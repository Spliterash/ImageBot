package ru.spliterash.imageBot.messengers.vk.fixes;

import com.google.gson.Gson;
import com.vk.api.sdk.objects.photos.responses.PhotoUploadResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.domain.utils.ArrayUtils;
import ru.spliterash.imageBot.domain.utils.ThreadUtils;
import ru.spliterash.imageBot.messengers.domain.exceptions.MessengerExceptionWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class UploadMultiFiles {
    private final ThreadUtils threadUtils;

    public UploadMultiFiles(ThreadUtils threadUtils) {
        this.threadUtils = threadUtils;
        HttpClientBuilder builder = HttpClientBuilder.create();
        httpClient = builder.build();
    }

    private final HttpClient httpClient;
    private final Gson gson = new Gson();

    public List<PhotoUploadResponse> uploadImageFilesToVk(String uploadUrl, List<ImageData> toUploadAll, int peerId) throws IOException {
        List<List<ImageData>> chunks = ArrayUtils.batches(toUploadAll, 5).collect(Collectors.toList());

        return threadUtils.mapAsyncBlocked(chunks, toUpload -> {
            HttpPost httpPost = new HttpPost(uploadUrl);

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            for (int i = 0; i < 5 && i < toUpload.size(); i++) {
                ImageData data = toUpload.get(i);

                String fieldName = "file" + (i + 1);
                entityBuilder.addPart(fieldName, new InputStreamBody(data.read(), ContentType.IMAGE_PNG, "image" + i + ".png"));
            }

            final HttpEntity entity = entityBuilder.build();
            httpPost.setEntity(entity);
            try {
                HttpResponse response = httpClient.execute(httpPost);

                String strResponse = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

                return gson.fromJson(strResponse, PhotoUploadResponse.class);
            } catch (IOException exception) {
                throw new MessengerExceptionWrapper("Vk file upload error", String.valueOf(peerId), exception);
            }
        });
    }
}
