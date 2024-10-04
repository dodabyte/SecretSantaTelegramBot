package com.example.secretsantatelegrambot.service;

import com.example.secretsantatelegrambot.entity.GiftAssignment;

import java.util.List;
import java.util.UUID;

public interface RandomService {
    List<GiftAssignment> startRandomization(UUID roomId, Long chatId);
}
