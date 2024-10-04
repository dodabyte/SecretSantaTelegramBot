package com.example.secretsantatelegrambot.exception.impl;

public class UserNotAllowedException extends RuntimeException {
    private static final String USER_NOT_ALLOWED_MESSAGE = "Пользователь не имеет возможности выполнить выбранное действие.";

    public UserNotAllowedException() {
        super(USER_NOT_ALLOWED_MESSAGE);
    }

    public UserNotAllowedException(String message) {
        super(message);
    }
}
