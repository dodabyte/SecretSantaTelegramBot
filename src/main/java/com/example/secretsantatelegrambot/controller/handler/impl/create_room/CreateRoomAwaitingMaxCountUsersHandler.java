package com.example.secretsantatelegrambot.controller.handler.impl.create_room;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.exception.impl.RoomMaxCountUsersFormatException;
import com.example.secretsantatelegrambot.service.UserService;
import com.example.secretsantatelegrambot.util.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.example.secretsantatelegrambot.util.AppCache.ENTERED_COUNTS_USERS_IN_ROOM_CACHE;
import static com.example.secretsantatelegrambot.util.AppCache.MESSAGE_ID_CACHE;
import static com.example.secretsantatelegrambot.util.AppCache.getMessageIdFromCache;
import static com.example.secretsantatelegrambot.util.CommandUtil.CANCEL_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.CANCEL_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.createFormattedCommand;
import static com.example.secretsantatelegrambot.util.MessageUtil.CREATE_ROOM_WITH_MIN_COST_GIFT_MESSAGE;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createButton;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createKeyboardWithButtonsInRow;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createSendMessage;
import static com.example.secretsantatelegrambot.util.TelegramUtil.deleteMessageReplyMarkup;

@Component
@RequiredArgsConstructor
public class CreateRoomAwaitingMaxCountUsersHandler implements Handler {
    private final UserService userService;
    private final Validator validator;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        Long chatId = user.getChatId();
        int maxCountUsers = getMaxCountUsers(user.getId(), message);

        ENTERED_COUNTS_USERS_IN_ROOM_CACHE.get(user.getId()).add(maxCountUsers);

        EditMessageReplyMarkup editPreviousMessage = deleteMessageReplyMarkup(chatId, getMessageIdFromCache(user.getId()));
        MESSAGE_ID_CACHE.put(user.getId(), messageId + 1);

        SendMessage createRoomMessage = createSendMessage(chatId, CREATE_ROOM_WITH_MIN_COST_GIFT_MESSAGE);
        createRoomMessage.setReplyMarkup(createKeyboardWithButtonsInRow(
                List.of(createButton(CANCEL_BUTTON_MESSAGE, createFormattedCommand(CANCEL_COMMAND, String.valueOf(true))))));

        userService.setUserState(user, UserState.AWAITING_CREATE_MIN_COST_GIFT_ROOM);

        return List.of(editPreviousMessage, createRoomMessage);
    }

    @Override
    public UserState operatedBotState() {
        return UserState.AWAITING_CREATE_MAX_COUNT_USERS_ROOM;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }

    private int getMaxCountUsers(UUID userId, String message) {
        int maxCountUsers;

        try {
            maxCountUsers = Integer.parseInt(message);
        }
        catch (NumberFormatException e) {
            throw new RoomMaxCountUsersFormatException();
        }

        if (!validator.validateEnteredRoomMaxCountUsersFormat(maxCountUsers,
                ENTERED_COUNTS_USERS_IN_ROOM_CACHE.get(userId).get(0))) {
            throw new RoomMaxCountUsersFormatException();
        }

        return maxCountUsers;
    }
}
