package com.example.secretsantatelegrambot.controller.handler.impl.start_randomization;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.entity.Room;
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

import static com.example.secretsantatelegrambot.util.CommandUtil.CANCEL_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.CONFIRM_RANDOMIZATION_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.NO_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.START_RANDOMIZATION_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.YES_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.createFormattedCommand;
import static com.example.secretsantatelegrambot.util.CommandUtil.getRoomIdFromCommand;
import static com.example.secretsantatelegrambot.util.MessageUtil.START_RANDOMIZATION_CONFIRM_MESSAGE;
import static com.example.secretsantatelegrambot.util.MessageUtil.escape;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createEditMessageText;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createButton;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createKeyboardWithButtonsInRow;

@Component
@RequiredArgsConstructor
public class StartRandomizationStartHandler implements Handler {
    private final UserService userService;
    private final RoomService roomService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        Long chatId = user.getChatId();
        UUID roomId = getRoomIdFromCommand(chatId, message);

        Room room = roomService.getRoomByRoomId(roomId, chatId);

        EditMessageText startRandomizationMessage = createEditMessageText(chatId, messageId,
                START_RANDOMIZATION_CONFIRM_MESSAGE.formatted(escape(room.getName())));
        startRandomizationMessage.setReplyMarkup(createKeyboardWithButtonsInRow(List.of(
                createButton(YES_BUTTON_MESSAGE, createFormattedCommand(CONFIRM_RANDOMIZATION_COMMAND, roomId.toString())),
                createButton(NO_BUTTON_MESSAGE, createFormattedCommand(CANCEL_COMMAND, roomId.toString(), String.valueOf(false))))));

        userService.setUserState(user, UserState.AWAITING_START_RANDOMIZATION);

        return List.of(startRandomizationMessage);
    }

    @Override
    public UserState operatedBotState() {
        return UserState.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(START_RANDOMIZATION_COMMAND);
    }
}
