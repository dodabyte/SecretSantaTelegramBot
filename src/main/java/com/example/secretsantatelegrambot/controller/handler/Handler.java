package com.example.secretsantatelegrambot.controller.handler;

import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.List;

public interface Handler {
    List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message);

    UserState operatedBotState();

    List<String> operatedCallBackQuery();
}
