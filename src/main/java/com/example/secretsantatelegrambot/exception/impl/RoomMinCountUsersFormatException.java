package com.example.secretsantatelegrambot.exception.impl;

import com.example.secretsantatelegrambot.exception.SecretSantaException;

public class RoomMinCountUsersFormatException extends SecretSantaException {
    private static final String ROOM_MIN_COUNT_USERS_FORMAT_MESSAGE = "Неверный формат для минимального количества участников комнаты.";

    public RoomMinCountUsersFormatException() {
        super(ROOM_MIN_COUNT_USERS_FORMAT_MESSAGE);
    }

    public RoomMinCountUsersFormatException(String message) {
        super(message);
    }
}
