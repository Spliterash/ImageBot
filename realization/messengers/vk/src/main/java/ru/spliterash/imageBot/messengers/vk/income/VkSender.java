package ru.spliterash.imageBot.messengers.vk.income;

import com.vk.api.sdk.objects.users.UserFull;
import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.messengers.domain.message.income.IncomeMessage;
import ru.spliterash.imageBot.messengers.vk.VkUserInfoService;

@RequiredArgsConstructor
public class VkSender implements IncomeMessage.Sender {
    private final int id;
    private final VkUserInfoService vkUserInfoService;

    @Override
    public String getId() {
        return String.valueOf(id);
    }

    @Override
    public String getName() {
        UserFull userFull = vkUserInfoService.get(id);

        return userFull.getFirstName() + " " + userFull.getLastName();
    }
}
