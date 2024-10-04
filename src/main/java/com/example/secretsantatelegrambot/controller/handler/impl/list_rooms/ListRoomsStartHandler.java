package com.example.secretsantatelegrambot.controller.handler.impl.list_rooms;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.entity.Room;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.secretsantatelegrambot.util.CommandUtil.BACK_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.CANCEL_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.GET_ALL_USER_ROOMS_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.ROOM_DETAILS_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.createFormattedCommand;
import static com.example.secretsantatelegrambot.util.MessageUtil.LIST_ROOMS_MESSAGE;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createEditMessageText;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createButton;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createKeyboardWithButtonsInColumn;

@Component
@RequiredArgsConstructor
public class ListRoomsStartHandler implements Handler {
    private final RoomService roomService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        Set<Room> rooms = roomService.getAllUserRooms(user.getChatId());

        EditMessageText listRoomsMessage = createEditMessageText(user.getChatId(), messageId, LIST_ROOMS_MESSAGE);
        listRoomsMessage.setReplyMarkup(createInlineKeyboardWithListRooms(rooms));

        return List.of(listRoomsMessage);
    }

    @Override
    public UserState operatedBotState() {
        return UserState.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(GET_ALL_USER_ROOMS_COMMAND);
    }

    private InlineKeyboardMarkup createInlineKeyboardWithListRooms(Set<Room> rooms) {
        List<List<InlineKeyboardButton>> buttonsWithRooms = new ArrayList<>();
        if (!rooms.isEmpty()) {
            for (Room room : rooms) {
                buttonsWithRooms.add(List.of(createButton(room.getName(),
                        createFormattedCommand(ROOM_DETAILS_COMMAND, room.getId().toString()))));
            }
        }
        buttonsWithRooms.add(List.of(createButton(BACK_BUTTON_MESSAGE, createFormattedCommand(CANCEL_COMMAND, String.valueOf(false)))));

        return createKeyboardWithButtonsInColumn(buttonsWithRooms);
    }
}
