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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class UploadMultiFiles {

    public UploadMultiFiles() {
        HttpClientBuilder builder = HttpClientBuilder.create();
        httpClient = builder.build();
    }

    private final HttpClient httpClient;
    private final Gson gson = new Gson();

    public PhotoUploadResponse uploadImageFilesToVk(String uploadUrl, List<ImageData> toUpload) throws IOException {
        HttpPost httpPost = new HttpPost(uploadUrl);

        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        for (int i = 0; i < 5 && i < toUpload.size(); i++) {
            ImageData data = toUpload.get(i);

            String fieldName = "file" + (i + 1);
            entityBuilder.addPart(fieldName, new InputStreamBody(data.read(), ContentType.IMAGE_PNG, "image" + i + ".png"));
        }

        final HttpEntity entity = entityBuilder.build();
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost);

        String strResponse = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

        return gson.fromJson(strResponse, PhotoUploadResponse.class);
    }
}
