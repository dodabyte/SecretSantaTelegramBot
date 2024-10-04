package com.example.secretsantatelegrambot.controller.handler.impl;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.secretsantatelegrambot.util.AppCache.MESSAGE_ID_CACHE;
import static com.example.secretsantatelegrambot.util.AppCache.checkAndRemoveUserCache;
import static com.example.secretsantatelegrambot.util.CommandUtil.CANCEL_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.splitCommand;
import static com.example.secretsantatelegrambot.util.MessageUtil.getStartMessage;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createEditMessageText;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createInlineKeyboardWithMainButtons;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createSendMessage;
import static com.example.secretsantatelegrambot.util.TelegramUtil.deleteMessageReplyMarkup;

@Component
@RequiredArgsConstructor
public class CancelAndGetStartHandler implements Handler {
    private final UserService userService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        UUID userId = user.getId();
        Long chatId = user.getChatId();
        boolean isNewMessage = Boolean.parseBoolean(splitCommand(message)[1]);

        checkAndRemoveUserCache(userId);

        List<PartialBotApiMethod<? extends Serializable>> messages = new ArrayList<>();

        if (isNewMessage) {
            EditMessageReplyMarkup editPreviousMessage = deleteMessageReplyMarkup(chatId, messageId);
            messages.add(editPreviousMessage);
        }
        MESSAGE_ID_CACHE.remove(userId);

        String startMessage = getStartMessage(user.getUsername());
        BotApiMethod cancelMessage = getCancelMessage(isNewMessage, chatId, messageId, startMessage);
        messages.add(cancelMessage);

        if (!UserState.START.equals(user.getUserState())) {
            userService.setUserState(user, UserState.START);
        }

        return messages;
    }

    @Override
    public UserState operatedBotState() {
        return UserState.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(CANCEL_COMMAND);
    }

    private SendMessage createSendCancelMessage(Long chatId, String message) {
        SendMessage sendCancelMessage = createSendMessage(chatId, message);
        sendCancelMessage.setReplyMarkup(createInlineKeyboardWithMainButtons());
        return sendCancelMessage;
    }

    private EditMessageText createEditCancelMessageText(Long chatId, Integer messageId, String message) {
        EditMessageText editCancelMessage = createEditMessageText(chatId, messageId, message);
        editCancelMessage.setReplyMarkup(createInlineKeyboardWithMainButtons());
        return editCancelMessage;
    }

    private BotApiMethod getCancelMessage(boolean isNewMessage, Long chatId, Integer messageId, String message) {
        BotApiMethod cancelMessage;
        if (isNewMessage) {
            cancelMessage = createSendCancelMessage(chatId, message);
        }
        else {
            cancelMessage = createEditCancelMessageText(chatId, messageId, message);
        }
        return cancelMessage;
    }
}
