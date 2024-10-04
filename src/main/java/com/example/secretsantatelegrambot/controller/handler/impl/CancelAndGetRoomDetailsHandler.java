package com.example.secretsantatelegrambot.controller.handler.impl;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.entity.Room;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.service.GiftAssignmentService;
import com.example.secretsantatelegrambot.service.RoomService;
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
import static com.example.secretsantatelegrambot.util.CommandUtil.getRoomIdFromText;
import static com.example.secretsantatelegrambot.util.CommandUtil.splitCommand;
import static com.example.secretsantatelegrambot.util.MessageUtil.createRoomDetailsMessage;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createEditMessageText;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createKeyboardWithRoomDetails;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createSendMessage;
import static com.example.secretsantatelegrambot.util.TelegramUtil.deleteMessageReplyMarkup;

@Component
@RequiredArgsConstructor
public class CancelAndGetRoomDetailsHandler implements Handler {
    private final UserService userService;
    private final RoomService roomService;
    private final GiftAssignmentService giftAssignmentService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        UUID userId = user.getId();
        Long chatId = user.getChatId();
        String[] args = splitCommand(message);

        if (args.length < 3) {
            throw new IllegalArgumentException();
        }

        UUID roomId = getRoomIdFromText(args[1]);
        boolean isNewMessage = Boolean.parseBoolean(splitCommand(message)[2]);

        Room room = roomService.getRoomByRoomId(roomId, chatId);
        boolean existsGiftAssignment = giftAssignmentService.existsByRoomId(roomId);

        checkAndRemoveUserCache(userId);

        List<PartialBotApiMethod<? extends Serializable>> messages = new ArrayList<>();

        if (isNewMessage) {
            EditMessageReplyMarkup editPreviousMessage = deleteMessageReplyMarkup(chatId, messageId);
            messages.add(editPreviousMessage);
        }
        MESSAGE_ID_CACHE.remove(userId);

        String roomDetailsMessage = createRoomDetailsMessage(room);
        BotApiMethod cancelMessage = getCancelMessage(isNewMessage, chatId, messageId, roomDetailsMessage, room, user, existsGiftAssignment);
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

    private SendMessage createSendCancelMessage(Long chatId, String message, Room room, User user, boolean existsGiftAssignment) {
        SendMessage sendCancelMessage = createSendMessage(chatId, message);
        sendCancelMessage.setReplyMarkup(createKeyboardWithRoomDetails(room, user, existsGiftAssignment));
        return sendCancelMessage;
    }

    private EditMessageText createEditCancelMessageText(Long chatId, Integer messageId, String message,
                                                        Room room, User user, boolean existsGiftAssignment) {
        EditMessageText editCancelMessage = createEditMessageText(chatId, messageId, message);
        editCancelMessage.setReplyMarkup(createKeyboardWithRoomDetails(room, user, existsGiftAssignment));
        return editCancelMessage;
    }

    private BotApiMethod getCancelMessage(boolean isNewMessage, Long chatId, Integer messageId, String message,
                                          Room room, User user, boolean existsGiftAssignment) {
        BotApiMethod cancelMessage;
        if (isNewMessage) {
            cancelMessage = createSendCancelMessage(chatId, message, room, user, existsGiftAssignment);
        }
        else {
            cancelMessage = createEditCancelMessageText(chatId, messageId, message, room, user, existsGiftAssignment);
        }
        return cancelMessage;
    }
}
