package com.example.secretsantatelegrambot.exception.impl;

import com.example.secretsantatelegrambot.exception.SecretSantaException;

public class CountUsersLessMinCountUsersException extends SecretSantaException {
    private static final String COUNT_USERS_LESS_MIN_COUNT_USERS_EXCEPTION_MESSAGE = "Количество пользователей в комнате недостаточно для начала рандомизации.";

    public CountUsersLessMinCountUsersException() {
        super(COUNT_USERS_LESS_MIN_COUNT_USERS_EXCEPTION_MESSAGE);
    }

    public CountUsersLessMinCountUsersException(String message, Long chatId) {
        super(message);
    }
}
