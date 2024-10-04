package com.example.secretsantatelegrambot.controller.handler.impl;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.List;

import static com.example.secretsantatelegrambot.util.CommandUtil.START_COMMAND;
import static com.example.secretsantatelegrambot.util.MessageUtil.getStartMessage;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createInlineKeyboardWithMainButtons;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createSendMessage;

@Component
@RequiredArgsConstructor
public class StartHandler implements Handler {
    private final UserService userService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        Long chatId = user.getChatId();

        SendMessage startMessage = createSendMessage(chatId, getStartMessage(user.getUsername()));
        startMessage.setReplyMarkup(createInlineKeyboardWithMainButtons());

        if (!UserState.START.equals(user.getUserState())) {
            userService.setUserState(user, UserState.START);
        }

        return List.of(startMessage);
    }

    @Override
    public UserState operatedBotState() {
        return UserState.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(START_COMMAND);
    }
}
