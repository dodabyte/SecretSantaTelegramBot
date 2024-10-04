package com.example.secretsantatelegrambot.exception.impl;

import com.example.secretsantatelegrambot.exception.SecretSantaException;

public class GiftAssignmentNotFoundException extends SecretSantaException {
    private static final String GIFT_ASSIGNMENT_NOT_FOUND_MESSAGE = "В этой комнтае участники не были распределены в качестве «дарителей» и «получателей».";

    public GiftAssignmentNotFoundException() {
        super(GIFT_ASSIGNMENT_NOT_FOUND_MESSAGE);
    }

    public GiftAssignmentNotFoundException(String message) {
        super(message);
    }
}
