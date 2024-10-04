package com.example.secretsantatelegrambot.service;

import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;

public interface UserService {
    User findByChatId(Long chatId);

    User findByUsername(String username);

    User findByChatIdOrSaveUser(Long chatId, String username);

    void setUserState(User user, UserState state);
}