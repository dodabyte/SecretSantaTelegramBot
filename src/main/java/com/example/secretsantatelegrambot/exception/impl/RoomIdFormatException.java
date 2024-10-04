package com.example.secretsantatelegrambot.exception.impl;

import com.example.secretsantatelegrambot.exception.SecretSantaException;

public class RoomIdFormatException extends SecretSantaException {
    private static final String ROOM_ID_FORMAT_EXCEPTION_MESSAGE = "Неверный формат идентификатора комнаты.";

    public RoomIdFormatException() {
        super(ROOM_ID_FORMAT_EXCEPTION_MESSAGE);
    }

    public RoomIdFormatException(String message) {
        super(message);
    }
}
