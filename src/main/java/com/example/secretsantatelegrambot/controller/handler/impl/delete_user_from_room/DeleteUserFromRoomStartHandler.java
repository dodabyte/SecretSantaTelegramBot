package com.example.secretsantatelegrambot.controller.handler.impl.delete_user_from_room;

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
import java.util.UUID;

import static com.example.secretsantatelegrambot.util.AppCache.MESSAGE_ID_CACHE;
import static com.example.secretsantatelegrambot.util.AppCache.ROOM_ID_CACHE;
import static com.example.secretsantatelegrambot.util.CommandUtil.BACK_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.CANCEL_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.DELETE_USER_FROM_ROOM_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.createFormattedCommand;
import static com.example.secretsantatelegrambot.util.CommandUtil.getRoomIdFromCommand;
import static com.example.secretsantatelegrambot.util.MessageUtil.DELETE_USER_FROM_ROOM_WITH_USERNAME_MESSAGE;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createButton;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createKeyboardWithButtonsInRow;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createSendMessage;

@Component
@RequiredArgsConstructor
public class DeleteUserFromRoomStartHandler implements Handler {
    private final UserService userService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        Long chatId = user.getChatId();
        UUID userId = user.getId();
        UUID roomId = getRoomIdFromCommand(chatId, message);

        ROOM_ID_CACHE.put(userId, roomId);
        MESSAGE_ID_CACHE.put(userId, messageId + 1);

        SendMessage deleteUserFromRoomMessage = createSendMessage(chatId, DELETE_USER_FROM_ROOM_WITH_USERNAME_MESSAGE);
        deleteUserFromRoomMessage.setReplyMarkup(createKeyboardWithButtonsInRow(
                List.of(createButton(BACK_BUTTON_MESSAGE,
                        createFormattedCommand(CANCEL_COMMAND, roomId.toString(), String.valueOf(false))))));

        userService.setUserState(user, UserState.AWAITING_USERNAME_DELETE);

        return List.of(deleteUserFromRoomMessage);
    }

    @Override
    public UserState operatedBotState() {
        return UserState.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(DELETE_USER_FROM_ROOM_COMMAND);
    }
}
