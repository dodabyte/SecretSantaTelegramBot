package com.example.secretsantatelegrambot.controller.handler.impl.join_room;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.entity.Room;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.service.RoomService;
import com.example.secretsantatelegrambot.service.UserService;
import com.example.secretsantatelegrambot.util.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.secretsantatelegrambot.util.CommandUtil.CANCEL_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.START_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.START_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.START_RANDOMIZATION_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.WAIT_OTHER_USERS_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.createFormattedCommand;
import static com.example.secretsantatelegrambot.util.CommandUtil.getRoomIdFromCommand;
import static com.example.secretsantatelegrambot.util.MessageUtil.JOIN_ROOM_MESSAGE;
import static com.example.secretsantatelegrambot.util.MessageUtil.JOIN_ROOM_START_RANDOMIZATION_CONFIRM_MESSAGE;
import static com.example.secretsantatelegrambot.util.MessageUtil.escape;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createButton;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createInlineKeyboardWithMainButtons;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createKeyboardWithButtonsInRow;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createSendMessage;

@Component
@RequiredArgsConstructor
public class StartAndJoinRoomHandler implements Handler {
    private final UserService userService;
    private final RoomService roomService;
    private final Validator validator;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        Long chatId = user.getChatId();
        UUID roomId = getRoomIdFromCommand(chatId, message);

        Room room = roomService.joinRoom(user, roomId);

        List<PartialBotApiMethod<? extends Serializable>> messages = new ArrayList<>();

        if (validator.validateMinCountUsersInRoom(room)) {
            Long adminChatId = room.getAdminUser().getChatId();
            messages.add(createSendAdminJoinRoomMessage(adminChatId, room.getName(), roomId.toString()));
            userService.setUserState(room.getAdminUser(), UserState.START);
        }

        messages.add(createSendJoinRoomMessage(chatId, user.getUsername(), room.getName()));

        userService.setUserState(user, UserState.START);

        return messages;
    }

    @Override
    public UserState operatedBotState() {
        return UserState.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(START_COMMAND);
    }

    private SendMessage createSendAdminJoinRoomMessage(Long adminChatId, String roomName, String roomId) {
        SendMessage sendJoinRoomMessage = createSendMessage(adminChatId,
                JOIN_ROOM_START_RANDOMIZATION_CONFIRM_MESSAGE.formatted(escape(roomName)));
        sendJoinRoomMessage.setReplyMarkup(createKeyboardWithButtonsInRow(List.of(
                createButton(START_BUTTON_MESSAGE, createFormattedCommand(START_RANDOMIZATION_COMMAND, roomId)),
                createButton(WAIT_OTHER_USERS_BUTTON_MESSAGE, createFormattedCommand(CANCEL_COMMAND, String.valueOf(true))))));
        return sendJoinRoomMessage;
    }

    private SendMessage createSendJoinRoomMessage(Long chatId, String username, String roomName) {
        SendMessage sendJoinRoomMessage = createSendMessage(chatId, JOIN_ROOM_MESSAGE.formatted(escape(username), escape(roomName)));
        sendJoinRoomMessage.setReplyMarkup(createInlineKeyboardWithMainButtons());
        return sendJoinRoomMessage;
    }
}
