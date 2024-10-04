package com.example.secretsantatelegrambot.controller.handler.impl.update_room;

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
import static com.example.secretsantatelegrambot.util.CommandUtil.CANCEL_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.CANCEL_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.createFormattedCommand;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createButton;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createKeyboardWithButtonsInRow;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createSendMessage;

@Component
@RequiredArgsConstructor
public class UpdateRoomAwaitingGlobalHandler {
    private final UserService userService;

    public List<PartialBotApiMethod<? extends Serializable>> getPartialBotApiMethods(User user, Integer messageId,
                                                                                     String messageToUser,
                                                                                     UserState state, UUID roomId) {
        MESSAGE_ID_CACHE.put(user.getId(), messageId + 1);

        SendMessage updateRoomMessage = createSendMessage(user.getChatId(), messageToUser);
        updateRoomMessage.setReplyMarkup(createKeyboardWithButtonsInRow(
                List.of(createButton(CANCEL_BUTTON_MESSAGE,
                        createFormattedCommand(CANCEL_COMMAND, roomId.toString(), String.valueOf(false))))));

        userService.setUserState(user, state);

        return List.of(updateRoomMessage);
    }
}