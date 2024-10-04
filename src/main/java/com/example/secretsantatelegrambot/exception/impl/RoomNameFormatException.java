package com.example.secretsantatelegrambot.exception.impl;

import com.example.secretsantatelegrambot.exception.SecretSantaException;

public class RoomNameFormatException extends SecretSantaException {
    private static final String ROOM_NAME_FORMAT_MESSAGE = "Неверный формат названия команты.";

    public RoomNameFormatException() {
        super(ROOM_NAME_FORMAT_MESSAGE);
    }

    public RoomNameFormatException(String message) {
        super(message);
    }
}
