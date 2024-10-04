package com.example.secretsantatelegrambot.exception.impl;

import com.example.secretsantatelegrambot.exception.SecretSantaException;

public class RoomMaxCountUsersFormatException extends SecretSantaException {
    private static final String ROOM_MAX_COUNT_USERS_FORMAT_MESSAGE = "Неверный формат для максимального количества участников комнаты.";

    public RoomMaxCountUsersFormatException() {
        super(ROOM_MAX_COUNT_USERS_FORMAT_MESSAGE);
    }

    public RoomMaxCountUsersFormatException(String message) {
        super(message);
    }
}
