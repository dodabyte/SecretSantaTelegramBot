package com.example.secretsantatelegrambot.exception.impl;

import com.example.secretsantatelegrambot.exception.SecretSantaException;

public class RoomMinCostGiftFormatException extends SecretSantaException {
    private static final String MIN_COST_GIFT_FORMAT_MESSAGE = "Неверный формат для минимальной стоимости подарков.";

    public RoomMinCostGiftFormatException() {
        super(MIN_COST_GIFT_FORMAT_MESSAGE);
    }

    public RoomMinCostGiftFormatException(String message) {
        super(message);
    }
}
