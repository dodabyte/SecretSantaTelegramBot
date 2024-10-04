package com.example.secretsantatelegrambot.exception.impl;

import com.example.secretsantatelegrambot.exception.SecretSantaException;

public class RoomNotFoundException extends SecretSantaException {
    private static final String ROOM_NOT_FOUND_MESSAGE = "Указанная комната не существует.";

    public RoomNotFoundException() {
        super(ROOM_NOT_FOUND_MESSAGE);
    }

    public RoomNotFoundException(String message) {
        super(message);
    }
}
