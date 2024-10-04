package com.example.secretsantatelegrambot.controller.handler.impl.create_room;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.exception.impl.RoomMinCostGiftFormatException;
import com.example.secretsantatelegrambot.service.RoomService;
import com.example.secretsantatelegrambot.service.UserService;
import com.example.secretsantatelegrambot.util.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.example.secretsantatelegrambot.util.AppCache.ENTERED_COUNTS_USERS_IN_ROOM_CACHE;
import static com.example.secretsantatelegrambot.util.AppCache.ENTERED_NAME_ROOM_CACHE;
import static com.example.secretsantatelegrambot.util.AppCache.MESSAGE_ID_CACHE;
import static com.example.secretsantatelegrambot.util.AppCache.checkAndRemoveUserCache;
import static com.example.secretsantatelegrambot.util.AppCache.getMessageIdFromCache;
import static com.example.secretsantatelegrambot.util.MessageUtil.getStartMessage;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createInlineKeyboardWithMainButtons;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createSendMessage;
import static com.example.secretsantatelegrambot.util.TelegramUtil.deleteMessageReplyMarkup;

@Component
@RequiredArgsConstructor
public class CreateRoomAwaitingMinCostGiftHandler implements Handler {
    private final UserService userService;
    private final RoomService roomService;
    private final Validator validator;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        Long chatId = user.getChatId();
        UUID userId = user.getId();
        BigDecimal minCostGift = getMinCostGift(message);

        roomService.createRoom(user, ENTERED_NAME_ROOM_CACHE.get(userId), ENTERED_COUNTS_USERS_IN_ROOM_CACHE.get(userId).get(0),
                ENTERED_COUNTS_USERS_IN_ROOM_CACHE.get(userId).get(1), minCostGift);

        EditMessageReplyMarkup editPreviousMessage = deleteMessageReplyMarkup(chatId, getMessageIdFromCache(user.getId()));
        MESSAGE_ID_CACHE.put(user.getId(), messageId + 1);

        SendMessage createRoomMessage = createSendMessage(chatId, getStartMessage(user.getUsername()));
        createRoomMessage.setReplyMarkup(createInlineKeyboardWithMainButtons());

        checkAndRemoveUserCache(userId);

        userService.setUserState(user, UserState.START);

        return List.of(editPreviousMessage, createRoomMessage);
    }

    @Override
    public UserState operatedBotState() {
        return UserState.AWAITING_CREATE_MIN_COST_GIFT_ROOM;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }

    private BigDecimal getMinCostGift(String message) {
        BigDecimal minCostGift;

        try {
            minCostGift = new BigDecimal(message);
        }
        catch (NumberFormatException e) {
            throw new RoomMinCostGiftFormatException();
        }

        if (!validator.validateEnteredMinCostGift(minCostGift)) {
            throw new RoomMinCostGiftFormatException();
        }

        return minCostGift;
    }
}
