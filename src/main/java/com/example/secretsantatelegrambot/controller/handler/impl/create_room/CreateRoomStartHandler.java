package com.example.secretsantatelegrambot.controller.handler.impl.create_room;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.secretsantatelegrambot.util.AppCache.ENTERED_COUNTS_USERS_IN_ROOM_CACHE;
import static com.example.secretsantatelegrambot.util.AppCache.MESSAGE_ID_CACHE;
import static com.example.secretsantatelegrambot.util.AppCache.getMessageIdFromCache;
import static com.example.secretsantatelegrambot.util.CommandUtil.CANCEL_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.CANCEL_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.CREATE_ROOM_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.createFormattedCommand;
import static com.example.secretsantatelegrambot.util.MessageUtil.CREATE_ROOM_WITH_NAME_ROOM_MESSAGE;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createButton;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createKeyboardWithButtonsInRow;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createSendMessage;
import static com.example.secretsantatelegrambot.util.TelegramUtil.deleteMessageReplyMarkup;

@Component
@RequiredArgsConstructor
public class CreateRoomStartHandler implements Handler {
    private final UserService userService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        Long chatId = user.getChatId();
        UUID userId = user.getId();
        ENTERED_COUNTS_USERS_IN_ROOM_CACHE.put(userId, new ArrayList<>());

        EditMessageReplyMarkup editPreviousMessage = deleteMessageReplyMarkup(chatId, getMessageIdFromCache(userId));
        MESSAGE_ID_CACHE.put(userId, messageId + 1);

        SendMessage createRoomMessage = createSendMessage(chatId, CREATE_ROOM_WITH_NAME_ROOM_MESSAGE);
        createRoomMessage.setReplyMarkup(createKeyboardWithButtonsInRow(
                List.of(createButton(CANCEL_BUTTON_MESSAGE, createFormattedCommand(CANCEL_COMMAND, String.valueOf(true))))));

        userService.setUserState(user, UserState.AWAITING_CREATE_NAME_ROOM);

        return List.of(editPreviousMessage, createRoomMessage);
    }


    @Override
    public UserState operatedBotState() {
        return UserState.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(CREATE_ROOM_COMMAND);
    }
}
