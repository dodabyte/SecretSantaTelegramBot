package com.example.secretsantatelegrambot.controller.handler.impl.leave_room;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.service.RoomService;
import com.example.secretsantatelegrambot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import static com.example.secretsantatelegrambot.util.CommandUtil.LEAVE_ROOM_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.getRoomIdFromCommand;
import static com.example.secretsantatelegrambot.util.MessageUtil.getStartMessage;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createEditMessageText;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createInlineKeyboardWithMainButtons;

@Component
@RequiredArgsConstructor
public class LeaveRoomConfirmHandler implements Handler {
    private final UserService userService;
    private final RoomService roomService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        Long chatId = user.getChatId();
        UUID roomId = getRoomIdFromCommand(chatId, message);

        roomService.deleteOrLeaveRoom(chatId, roomId);
        User updatedUser = userService.findByChatId(chatId);

        EditMessageText leaveRoomMessage = createEditMessageText(chatId, messageId, getStartMessage(updatedUser.getUsername()));
        leaveRoomMessage.setReplyMarkup(createInlineKeyboardWithMainButtons());

        userService.setUserState(updatedUser, UserState.START);

        return List.of(leaveRoomMessage);
    }

    @Override
    public UserState operatedBotState() {
        return UserState.AWAITING_CONFIRM_LEAVE_ROOM;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(LEAVE_ROOM_COMMAND);
    }
}
