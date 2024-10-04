package com.example.secretsantatelegrambot.exception.impl;

public class UserNotFoundException extends RuntimeException {
    private static final String USER_NOT_FOUND_MESSAGE = "Пользователь не найден.";

    public UserNotFoundException() {
        super(USER_NOT_FOUND_MESSAGE);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
