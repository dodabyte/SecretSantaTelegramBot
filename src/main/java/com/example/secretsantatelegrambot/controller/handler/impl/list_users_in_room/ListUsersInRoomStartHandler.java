package com.example.secretsantatelegrambot.controller.handler.impl.list_users_in_room;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.entity.Room;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.secretsantatelegrambot.util.CommandUtil.BACK_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.CANCEL_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.GET_ALL_USERS_IN_ROOM_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.createFormattedCommand;
import static com.example.secretsantatelegrambot.util.CommandUtil.getRoomIdFromCommand;
import static com.example.secretsantatelegrambot.util.MessageUtil.USERS_IN_ROOM_MESSAGE;
import static com.example.secretsantatelegrambot.util.MessageUtil.escape;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createEditMessageText;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createButton;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createKeyboardWithButtonsInRow;

@Component
@RequiredArgsConstructor
public class ListUsersInRoomStartHandler implements Handler {
    private final RoomService roomService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        Long chatId = user.getChatId();
        UUID roomId = getRoomIdFromCommand(chatId, message);

        Room room = roomService.getRoomByRoomId(roomId, chatId);

        EditMessageText listRoomsMessage = createEditMessageText(chatId, messageId,
                createFormattedUsersInRoomMessage(room));
        listRoomsMessage.setReplyMarkup(createKeyboardWithButtonsInRow(
                List.of(createButton(BACK_BUTTON_MESSAGE,
                        createFormattedCommand(CANCEL_COMMAND, roomId.toString(), String.valueOf(false))))));

        return List.of(listRoomsMessage);
    }

    @Override
    public UserState operatedBotState() {
        return UserState.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(GET_ALL_USERS_IN_ROOM_COMMAND);
    }

    private String createFormattedUsersInRoomMessage(Room room) {
        return USERS_IN_ROOM_MESSAGE.formatted(
                room.getUsers().stream()
                    .map(user -> "@" + escape(user.getUsername()))
                    .sorted(Comparator.naturalOrder())
                    .collect(Collectors.joining("\n"))
        );
    }
}
