package ru.spliterash.imageBot.messengers.vk;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.events.longpoll.GroupLongPollApi;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.docs.Doc;
import com.vk.api.sdk.objects.messages.Forward;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.MessageAttachment;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.PhotoSizes;
import com.vk.api.sdk.objects.photos.PhotoSizesType;
import com.vk.api.sdk.objects.photos.responses.GetMessagesUploadServerResponse;
import com.vk.api.sdk.objects.photos.responses.PhotoUploadResponse;
import com.vk.api.sdk.objects.photos.responses.SaveMessagesPhotoResponse;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.WallpostFull;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import org.apache.commons.lang3.StringUtils;
import ru.spliterash.imageBot.domain.def.CaseIO;
import ru.spliterash.imageBot.domain.def.bean.NoAutoCreate;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.domain.pipeline.PipelineService;
import ru.spliterash.imageBot.domain.utils.ThreadUtils;
import ru.spliterash.imageBot.messengers.domain.AbstractMessenger;
import ru.spliterash.imageBot.messengers.domain.attachment.income.*;
import ru.spliterash.imageBot.messengers.domain.message.income.IncomeMessage;
import ru.spliterash.imageBot.messengers.domain.message.outcome.OutcomeMessage;
import ru.spliterash.imageBot.messengers.domain.port.URLDownloader;
import ru.spliterash.imageBot.messengers.vk.exceptions.VkException;
import ru.spliterash.imageBot.messengers.vk.fixes.SaveGroupPhotoQuery;
import ru.spliterash.imageBot.messengers.vk.fixes.UploadMultiFiles;
import ru.spliterash.imageBot.messengers.vk.income.VkSender;
import ru.spliterash.imageBot.pipelines.text.TextPipelineGenerator;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.vk.api.sdk.objects.photos.PhotoSizesType.*;

@NoAutoCreate
public class VkMessenger extends AbstractMessenger {
    private final VkApiClient client;
    private final GroupActor actor;
    private final GroupLongPollApi longPullRunner;
    private final VkUserInfoService vkUserInfoService;
    private final UploadMultiFiles uploadMultiFiles;
    private static final List<PhotoSizesType> PHOTO_SIZES_PRIORITY = Arrays.asList(
            W, Z, Y, X, M, S
    );

    public VkMessenger(
            TextPipelineGenerator generator,
            URLDownloader urlDownloader,
            PipelineService pipelineService,
            ThreadUtils threadUtils,
            int groupId,
            String token
    ) {
        super(generator, urlDownloader, pipelineService, threadUtils);
        this.actor = new GroupActor(groupId, token);

        this.client = new VkApiClient(HttpTransportClient.getInstance());
        this.vkUserInfoService = new VkUserInfoService(client, actor);
        this.uploadMultiFiles = new UploadMultiFiles(threadUtils);
        longPullRunner = new GroupLongPollApi(client, actor, 0) {
            @Override
            protected void messageNew(Integer groupId, Message message) {
                parseVkMessage(message);
            }
        };
    }

    @Override
    public void postConstruct() {
        longPullRunner.run();
    }

    @Override
    public void preDestroy() {
        longPullRunner.stop();
    }

    private void parseVkMessage(Message message) {
        IncomeMessage msg = IncomeMessage.builder()
                .id(message.getConversationMessageId().toString())
                .text(message.getText())
                .peerId(message.getPeerId().toString())
                .sender(new VkSender(message.getFromId(), vkUserInfoService))
                .attachments(parseVkAttachments(message.getAttachments()))
                .build();

        pool.execute(() -> {
            try {
                notifyMessage(msg);
            } catch (Exception exception) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter writer = new PrintWriter(stringWriter);

                exception.printStackTrace(writer);

                sendMessage(OutcomeMessage.builder()
                        .text(stringWriter.toString())
                        .peerId(message.getPeerId().toString())
                        .build());
            }
        });
    }

    private List<IncomeAttachment> parseVkAttachments(List<MessageAttachment> vkAttachments) {
        List<IncomeAttachment> list = new ArrayList<>(vkAttachments.size());

        for (MessageAttachment attachment : vkAttachments) {
            switch (attachment.getType()) {
                case PHOTO:
                    list.add(parsePhotoAttachment(attachment.getPhoto()));
                    break;
                case WALL:
                    list.add(parseWall(attachment.getWall()));
                    break;
                case DOC:
                    parseDocAttachment(attachment.getDoc()).ifPresent(list::add);
                    break;
            }
        }

        return list;
    }

    private IncomePostAttachment parseWall(WallpostFull wall) {
        List<IncomeAttachment> list = new ArrayList<>(wall.getAttachments().size() + 1);

        IncomePostAttachment.IncomePostAttachmentBuilder<?, ?> builder = IncomePostAttachment.builder();

        if (StringUtils.isNotBlank(wall.getText()))
            builder.text(wall.getText());

        builder.id(String.valueOf(wall.getId()));


        for (WallpostAttachment attachment : wall.getAttachments()) {
            switch (attachment.getType()) {
                case PHOTO:
                    Photo photo = attachment.getPhoto();
                    list.add(parsePhotoAttachment(photo));
                    break;
                case DOC:
                    Doc doc = attachment.getDoc();
                    parseDocAttachment(doc).ifPresent(list::add);
                    break;
            }
        }

        builder.attachments(list);

        return builder.build();
    }

    private Optional<IncomeAttachment> parseDocAttachment(Doc doc) {
        if (doc.getType() == 4) {
            return Optional.of(UnknownIncomeImageAttachment.builder()
                    .id(String.valueOf(doc.getId()))
                    .url(doc.getUrl().toString())
                    .build());
        } else
            return Optional.empty();
    }

    private IncomeImageAttachment parsePhotoAttachment(Photo photo) {
        List<PhotoSizes> sizes = photo.getSizes();

        PhotoSizes biggerPhoto = sizes
                .stream()
                .min(Comparator.comparing(p -> {
                    int index = PHOTO_SIZES_PRIORITY.indexOf(p.getType());
                    if (index != -1)
                        return index;
                    else
                        return Integer.MAX_VALUE;
                }))
                .orElseThrow();

        return KnownIncomeImageAttachment.builder()
                .url(biggerPhoto.getUrl().toString())
                .width(biggerPhoto.getWidth())
                .height(biggerPhoto.getHeight())
                .build();
    }

    @Override
    public void sendMessage(OutcomeMessage message) {
        try {
            int peerId = Integer.parseInt(message.getPeerId());
            MessagesSendQuery builder = client
                    .messages()
                    .send(actor)
                    .peerId(peerId)
                    .message(message.getText())
                    .randomId(ThreadLocalRandom.current().nextInt())
                    .disableMentions(true);

            if (message.getReplyTo() != null) {
                Forward forward = new Forward();

                forward.setConversationMessageIds(List.of(Integer.parseInt(message.getReplyTo())));
                forward.setPeerId(peerId);

                builder.forward(forward);
            }

            String attachment = uploadAllAttachments(peerId, message.getAttachments());

            builder.attachment(attachment);

            builder.execute();
        } catch (ClientException | ApiException | IOException e) {
            throw new VkException(e);
        }
    }

    private String uploadAllAttachments(int peerId, CaseIO attachments) throws ClientException, IOException, ApiException {
        List<ImageData> images = attachments.get(ImageData.class).getNeedData();

        return uploadPhotos(peerId, images);
    }

    private String uploadPhotos(int peerId, List<ImageData> data) throws ClientException, ApiException, IOException {
        GetMessagesUploadServerResponse response = client
                .photos()
                .getMessagesUploadServer(actor)
                .peerId(peerId)
                .execute();

        URI url = response.getUploadUrl();


        List<SaveMessagesPhotoResponse> list = new ArrayList<>(data.size() / 5);
        for (PhotoUploadResponse uploadResponse : uploadMultiFiles.uploadImageFilesToVk(url.toString(), data)) {
            List<SaveMessagesPhotoResponse> responses = new SaveGroupPhotoQuery(client, actor)
                    .hash(uploadResponse.getHash())
                    .server(uploadResponse.getServer())
                    .photosList(uploadResponse.getPhoto())
                    .execute();

            list.addAll(responses);
        }


        return list
                .stream()
                .map(r -> "photo" + r.getOwnerId() + "_" + r.getId())
                .collect(Collectors.joining(","));
    }
}
