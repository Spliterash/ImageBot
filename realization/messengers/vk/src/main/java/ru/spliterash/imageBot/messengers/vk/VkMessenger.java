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
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import ru.spliterash.imageBot.domain.def.CaseIO;
import ru.spliterash.imageBot.domain.def.bean.NoAutoCreate;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.domain.pipeline.PipelineService;
import ru.spliterash.imageBot.messengers.domain.AbstractMessenger;
import ru.spliterash.imageBot.messengers.domain.attachment.income.*;
import ru.spliterash.imageBot.messengers.domain.message.income.IncomeMessage;
import ru.spliterash.imageBot.messengers.domain.message.outcome.OutcomeMessage;
import ru.spliterash.imageBot.messengers.domain.port.URLDownloader;
import ru.spliterash.imageBot.messengers.vk.exceptions.VkException;
import ru.spliterash.imageBot.messengers.vk.income.VkSender;
import ru.spliterash.imageBot.pipelines.text.TextPipelineGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.*;

import static com.vk.api.sdk.objects.photos.PhotoSizesType.*;

@NoAutoCreate
public class VkMessenger extends AbstractMessenger {
    private final VkApiClient client;
    private final GroupActor actor;
    private final GroupLongPollApi longPullRunner;
    private final VkUserInfoService vkUserInfoService;
    private static final List<PhotoSizesType> PHOTO_SIZES_PRIORITY = Arrays.asList(
            W, Z, Y, X, M, S
    );

    public VkMessenger(
            TextPipelineGenerator generator,
            URLDownloader urlDownloader,
            PipelineService pipelineService,
            int groupId,
            String token
    ) {
        super(generator, urlDownloader, pipelineService);
        this.actor = new GroupActor(groupId, token);

        this.client = new VkApiClient(HttpTransportClient.getInstance());
        this.vkUserInfoService = new VkUserInfoService(client, actor);

        longPullRunner = new GroupLongPollApi(client, actor, 0) {
            @Override
            protected void messageNew(Integer groupId, Message message) {
                parseVkMessage(message);
            }
        };

        longPullRunner.run();
    }

    @Override
    public void preDestroy() {
        longPullRunner.stop();
    }

    private void parseVkMessage(Message message) {
        IncomeMessage.builder()
                .id(message.getConversationMessageId().toString())
                .text(message.getText())
                .peerId(message.getPeerId().toString())
                .sender(new VkSender(message.getFromId(), vkUserInfoService))
                .attachments(parseVkAttachments(message.getAttachments()))
                .build();
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
                .min(Comparator.comparing(p -> PHOTO_SIZES_PRIORITY.indexOf(p.getType())))
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
                    .disableMentions(true);

            if (message.getReplyTo() != null) {
                Forward forward = new Forward();

                forward.setConversationMessageIds(List.of(Integer.parseInt(message.getReplyTo())));
                forward.setPeerId(peerId);

                builder.forward(forward);
            }

            List<String> attachment = uploadAllAttachments(peerId, message.getAttachments());

            builder.attachment(String.join(",", attachment));

            builder.execute();
        } catch (ClientException | ApiException | IOException e) {
            throw new VkException(e);
        }
    }

    private List<String> uploadAllAttachments(int peerId, CaseIO attachments) throws ClientException, IOException, ApiException {
        List<ImageData> images = attachments.get(ImageData.class).getNeedData();
        List<String> ids = new ArrayList<>(images.size());

        for (ImageData image : images) {
            uploadPhoto(peerId, image);
        }

        return ids;
    }

    private String uploadPhoto(int peerId, ImageData data) throws ClientException, ApiException, IOException {
        GetMessagesUploadServerResponse response = client
                .photos()
                .getMessagesUploadServer(actor)
                .peerId(peerId)
                .execute();

        URI url = response.getUploadUrl();

        File upload = File.createTempFile("upload", null);
        upload.deleteOnExit();

        IOUtils.copy(data.read(), new FileOutputStream(upload, false));

        PhotoUploadResponse uploadResponse = client
                .upload()
                .photo(url.toString(), upload)
                .file(upload)
                .execute();


        SaveMessagesPhotoResponse finalResponse = client
                .photos()
                .saveMessagesPhoto(actor, uploadResponse.getPhoto())
                .hash(uploadResponse.getHash())
                .server(uploadResponse.getServer())
                .execute()
                .stream()
                .findFirst()
                .orElseThrow();

        return "photo-" + finalResponse.getOwnerId() + "_" + finalResponse.getId();
    }
}
