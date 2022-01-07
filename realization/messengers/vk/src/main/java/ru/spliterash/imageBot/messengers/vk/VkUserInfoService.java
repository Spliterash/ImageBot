package ru.spliterash.imageBot.messengers.vk;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserFull;
import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.messengers.vk.exceptions.VkException;
import ru.spliterash.imageBot.messengers.vk.exceptions.VkUserNotFound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class VkUserInfoService {
    private final VkApiClient client;
    private final GroupActor actor;

    private final Map<Integer, UserFull> userMap = new HashMap<>();


    public UserFull get(int id) {
        return userMap.computeIfAbsent(id, this::getFromVK);
    }

    private UserFull getFromVK(int id) {
        try {
            return client
                    .users()
                    .get(actor)
                    .userIds(List.of(String.valueOf(id)))
                    .execute()
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new VkUserNotFound(id));
        } catch (ClientException | ApiException e) {
            throw new VkException(e);
        }
    }
}
