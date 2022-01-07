package ru.spliterash.imageBot.messengers.vk.fixes;

import com.vk.api.sdk.client.AbstractQueryBuilder;
import com.vk.api.sdk.client.Utils;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.objects.photos.responses.SaveMessagesPhotoResponse;

import java.util.Arrays;
import java.util.List;

public class SaveGroupPhotoQuery extends AbstractQueryBuilder<SaveGroupPhotoQuery, List<SaveMessagesPhotoResponse>> {

    public SaveGroupPhotoQuery(VkApiClient client, GroupActor actor) {
        super(client, "photos.saveMessagesPhoto", Utils.buildParametrizedType(List.class, SaveMessagesPhotoResponse.class));
        accessToken(actor.getAccessToken());
    }

    @Override
    protected SaveGroupPhotoQuery getThis() {
        return this;
    }

    @Override
    protected List<String> essentialKeys() {
        return Arrays.asList("access_token");
    }


    public SaveGroupPhotoQuery albumId(Integer value) {
        return unsafeParam("album_id", value);
    }

    /**
     * ID of the community to save photos to.
     *
     * @param value value of "group id" parameter.
     * @return a reference to this {@code AbstractQueryBuilder} object to fulfill the "Builder" pattern.
     */
    public SaveGroupPhotoQuery groupId(Integer value) {
        return unsafeParam("group_id", value);
    }

    /**
     * Parameter returned when photos are [vk.com/dev/upload_files|uploaded to server].
     *
     * @param value value of "server" parameter.
     * @return a reference to this {@code AbstractQueryBuilder} object to fulfill the "Builder" pattern.
     */
    public SaveGroupPhotoQuery server(Integer value) {
        return unsafeParam("server", value);
    }

    /**
     * Parameter returned when photos are [vk.com/dev/upload_files|uploaded to server].
     *
     * @param value value of "photos list" parameter.
     * @return a reference to this {@code AbstractQueryBuilder} object to fulfill the "Builder" pattern.
     */
    public SaveGroupPhotoQuery photosList(String value) {
        unsafeParam("photos_list", value);
        unsafeParam("photo", value);

        return this;
    }

    /**
     * Parameter returned when photos are [vk.com/dev/upload_files|uploaded to server].
     *
     * @param value value of "hash" parameter.
     * @return a reference to this {@code AbstractQueryBuilder} object to fulfill the "Builder" pattern.
     */
    public SaveGroupPhotoQuery hash(String value) {
        return unsafeParam("hash", value);
    }

    /**
     * Geographical latitude, in degrees (from '-90' to '90').
     *
     * @param value value of "latitude" parameter.
     * @return a reference to this {@code AbstractQueryBuilder} object to fulfill the "Builder" pattern.
     */
    public SaveGroupPhotoQuery latitude(Number value) {
        return unsafeParam("latitude", value);
    }

    /**
     * Geographical longitude, in degrees (from '-180' to '180').
     *
     * @param value value of "longitude" parameter.
     * @return a reference to this {@code AbstractQueryBuilder} object to fulfill the "Builder" pattern.
     */
    public SaveGroupPhotoQuery longitude(Number value) {
        return unsafeParam("longitude", value);
    }

    /**
     * Text describing the photo. 2048 digits max.
     *
     * @param value value of "caption" parameter.
     * @return a reference to this {@code AbstractQueryBuilder} object to fulfill the "Builder" pattern.
     */
    public SaveGroupPhotoQuery caption(String value) {
        return unsafeParam("caption", value);
    }

    public static class Response {

    }
}
