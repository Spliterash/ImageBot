package ru.spliterash.imageBot.messengers.vk;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserFull;
import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.messengers.domain.exceptions.MessengerExceptionWrapper;
import ru.spliterash.imageBot.messengers.domain.exceptions.MessengerUserNotFound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class VkUserInfoService {
    private final VkApiClient client;
    private final GroupActor actor;

    private final Map<Integer, UserFull> userMap = new HashMap<>();


    public UserFull get(int id, int peerId) {
        return userMap.computeIfAbsent(id, (ignore) -> getFromVK(id, peerId));
    }

    private UserFull getFromVK(int id, int peerId) {
        try {
            return client
                    .users()
                    .get(actor)
                    .userIds(List.of(String.valueOf(id)))
                    .execute()
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new MessengerUserNotFound(String.valueOf(id)));
        } catch (ClientException | ApiException e) {
            throw new MessengerExceptionWrapper("Ошибка получения пользователя", String.valueOf(peerId), e);
        }
    }
}
