package com.example.secretsantatelegrambot.exception.impl;

import com.example.secretsantatelegrambot.exception.SecretSantaException;

public class RoomReachedMaxCountUsersException extends SecretSantaException {
    private static final String ROOM_REACHED_MAX_COUNT_USERS_EXCEPTION = "Комната достигла максимального количества людей.";

    public RoomReachedMaxCountUsersException() {
        super(ROOM_REACHED_MAX_COUNT_USERS_EXCEPTION);
    }

    public RoomReachedMaxCountUsersException(String message) {
        super(message);
    }
}
