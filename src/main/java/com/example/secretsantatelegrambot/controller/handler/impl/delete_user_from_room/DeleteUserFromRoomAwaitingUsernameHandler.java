package com.example.secretsantatelegrambot.controller.handler.impl.delete_user_from_room;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.entity.Room;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.service.RoomService;
import com.example.secretsantatelegrambot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import static com.example.secretsantatelegrambot.util.AppCache.ENTERED_USERNAME_CACHE;
import static com.example.secretsantatelegrambot.util.AppCache.MESSAGE_ID_CACHE;
import static com.example.secretsantatelegrambot.util.AppCache.ROOM_ID_CACHE;
import static com.example.secretsantatelegrambot.util.AppCache.getMessageIdFromCache;
import static com.example.secretsantatelegrambot.util.CommandUtil.CANCEL_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.DELETE_USER_FROM_ROOM_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.NO_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.YES_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.createFormattedCommand;
import static com.example.secretsantatelegrambot.util.CommandUtil.getUsernameFromText;
import static com.example.secretsantatelegrambot.util.MessageUtil.DELETE_USER_FROM_ROOM_CONFIRM_MESSAGE;
import static com.example.secretsantatelegrambot.util.MessageUtil.escape;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createButton;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createKeyboardWithButtonsInRow;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createSendMessage;
import static com.example.secretsantatelegrambot.util.TelegramUtil.deleteMessageReplyMarkup;

@Component
@RequiredArgsConstructor
public class DeleteUserFromRoomAwaitingUsernameHandler implements Handler {
    private final RoomService roomService;
    private final UserService userService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        String enteredUsername = getUsernameFromText(message);
        Long chatId = user.getChatId();
        UUID userId = user.getId();
        UUID roomId = ROOM_ID_CACHE.get(userId);

        ENTERED_USERNAME_CACHE.put(userId, enteredUsername);

        Room room = roomService.getRoomByRoomId(roomId, chatId);

        EditMessageReplyMarkup editPreviousMessage = deleteMessageReplyMarkup(chatId, getMessageIdFromCache(userId));
        MESSAGE_ID_CACHE.put(userId, messageId + 1);

        SendMessage deleteUserFromRoomMessage = createSendMessage(chatId,
                DELETE_USER_FROM_ROOM_CONFIRM_MESSAGE.formatted(escape(getUsernameFromText(enteredUsername)), escape(room.getName())));
        deleteUserFromRoomMessage.setReplyMarkup(createKeyboardWithButtonsInRow(List.of(
                createButton(YES_BUTTON_MESSAGE,
                        createFormattedCommand(DELETE_USER_FROM_ROOM_COMMAND, roomId.toString())),
                createButton(NO_BUTTON_MESSAGE, createFormattedCommand(CANCEL_COMMAND, roomId.toString(), String.valueOf(true)))
        )));

        userService.setUserState(user, UserState.AWAITING_DELETE_CONFIRM);

        return List.of(editPreviousMessage, deleteUserFromRoomMessage);
    }

    @Override
    public UserState operatedBotState() {
        return UserState.AWAITING_USERNAME_DELETE;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(DELETE_USER_FROM_ROOM_COMMAND);
    }
}
