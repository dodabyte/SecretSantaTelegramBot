package com.example.secretsantatelegrambot.controller.handler.impl.update_room;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import static com.example.secretsantatelegrambot.util.AppCache.ROOM_ID_CACHE;
import static com.example.secretsantatelegrambot.util.CommandUtil.CANCEL_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.CANCEL_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.UPDATE_ROOM_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.UPDATE_ROOM_MAX_COUNT_USERS_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.UPDATE_ROOM_MAX_COUNT_USERS_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.UPDATE_ROOM_MIN_COST_GIFT_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.UPDATE_ROOM_MIN_COST_GIFT_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.UPDATE_ROOM_MIN_COUNT_USERS_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.UPDATE_ROOM_MIN_COUNT_USERS_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.UPDATE_ROOM_NAME_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.UPDATE_ROOM_NAME_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.createFormattedCommand;
import static com.example.secretsantatelegrambot.util.CommandUtil.getRoomIdFromCommand;
import static com.example.secretsantatelegrambot.util.MessageUtil.UPDATE_ROOM_MESSAGE;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createButton;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createEditMessageText;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createKeyboardWithButtonsInColumn;

@Component
@RequiredArgsConstructor
public class UpdateRoomStartHandler implements Handler {
    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        Long chatId = user.getChatId();
        UUID roomId = getRoomIdFromCommand(chatId, message);

        ROOM_ID_CACHE.put(user.getId(), roomId);

        EditMessageText updateRoomMessage = createEditMessageText(chatId, messageId, UPDATE_ROOM_MESSAGE);
        updateRoomMessage.setReplyMarkup(createKeyboardForUpdateRoom(roomId));

        return List.of(updateRoomMessage);
    }

    @Override
    public UserState operatedBotState() {
        return UserState.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(UPDATE_ROOM_COMMAND);
    }

    private InlineKeyboardMarkup createKeyboardForUpdateRoom(UUID roomId) {
        String stringRoomId = roomId.toString();
        return createKeyboardWithButtonsInColumn(List.of(
                List.of(createButton(UPDATE_ROOM_NAME_BUTTON_MESSAGE,
                        createFormattedCommand(UPDATE_ROOM_NAME_COMMAND, stringRoomId))),
                List.of(createButton(UPDATE_ROOM_MIN_COUNT_USERS_BUTTON_MESSAGE,
                        createFormattedCommand(UPDATE_ROOM_MIN_COUNT_USERS_COMMAND, stringRoomId))),
                List.of(createButton(UPDATE_ROOM_MAX_COUNT_USERS_BUTTON_MESSAGE,
                        createFormattedCommand(UPDATE_ROOM_MAX_COUNT_USERS_COMMAND, stringRoomId))),
                List.of(createButton(UPDATE_ROOM_MIN_COST_GIFT_BUTTON_MESSAGE,
                        createFormattedCommand(UPDATE_ROOM_MIN_COST_GIFT_COMMAND, stringRoomId))),
                List.of(createButton(CANCEL_BUTTON_MESSAGE,
                        createFormattedCommand(CANCEL_COMMAND, stringRoomId, String.valueOf(false))))
        ));
    }
}
