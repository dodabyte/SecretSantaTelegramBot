package com.example.secretsantatelegrambot.controller.handler.impl.leave_room;

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
import java.util.List;
import java.util.UUID;

import static com.example.secretsantatelegrambot.util.CommandUtil.CANCEL_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.LEAVE_ROOM_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.NO_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.YES_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.createFormattedCommand;
import static com.example.secretsantatelegrambot.util.CommandUtil.getRoomIdFromCommand;
import static com.example.secretsantatelegrambot.util.MessageUtil.LEAVE_ROOM_ADMIN_CONFIRM_MESSAGE;
import static com.example.secretsantatelegrambot.util.MessageUtil.LEAVE_ROOM_USER_CONFIRM_MESSAGE;
import static com.example.secretsantatelegrambot.util.MessageUtil.escape;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createButton;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createKeyboardWithButtonsInRow;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createSendMessage;

@Component
@RequiredArgsConstructor
public class LeaveRoomStartHandler implements Handler {
    private final UserService userService;
    private final RoomService roomService;
    private final Validator validator;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        Long chatId = user.getChatId();
        UUID roomId = getRoomIdFromCommand(user.getChatId(), message);

        Room room = roomService.getRoomByRoomId(roomId, chatId);

        SendMessage leaveRoomMessage = createLeaveRoomMessage(room, user);

        userService.setUserState(user, UserState.AWAITING_CONFIRM_LEAVE_ROOM);

        return List.of(leaveRoomMessage);
    }

    @Override
    public UserState operatedBotState() {
        return UserState.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(LEAVE_ROOM_COMMAND);
    }

    private SendMessage createLeaveRoomMessage(Room room, User user) {
        String roomId = room.getId().toString();
        SendMessage leaveRoomMessage;

        String message;
        if (validator.validateAdminUserInRoom(room, user)) {
            message = LEAVE_ROOM_ADMIN_CONFIRM_MESSAGE;
        }
        else {
            message = LEAVE_ROOM_USER_CONFIRM_MESSAGE;
        }

        leaveRoomMessage = createSendMessage(user.getChatId(), message.formatted(escape(room.getName())));
        leaveRoomMessage.setReplyMarkup(createKeyboardWithButtonsInRow(List.of(
                createButton(YES_BUTTON_MESSAGE,
                        createFormattedCommand(LEAVE_ROOM_COMMAND, roomId)),
                createButton(NO_BUTTON_MESSAGE, createFormattedCommand(CANCEL_COMMAND, roomId, String.valueOf(false)))
        )));

        return leaveRoomMessage;
    }
}
