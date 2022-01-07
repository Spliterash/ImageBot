package ru.spliterash.imageBot.messengers.vk.exceptions;

public class VkUserNotFound extends VkException {
    public VkUserNotFound(int id) {
        super("Пользователь вк не найден по id " + id);
    }
}
