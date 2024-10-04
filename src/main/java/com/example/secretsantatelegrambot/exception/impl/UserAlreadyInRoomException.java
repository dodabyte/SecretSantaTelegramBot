package com.example.secretsantatelegrambot.exception.impl;

import com.example.secretsantatelegrambot.exception.SecretSantaException;

public class UserAlreadyInRoomException extends SecretSantaException {
    private static final String USER_ALREADY_IN_ROOM_MESSAGE = "Пользователь уже находится в указанной комнате.";

    public UserAlreadyInRoomException() {
        super(USER_ALREADY_IN_ROOM_MESSAGE);
    }

    public UserAlreadyInRoomException(String message) {
        super(message);
    }
}
