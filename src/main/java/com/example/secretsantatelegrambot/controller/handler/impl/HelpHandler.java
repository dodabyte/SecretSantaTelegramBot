package com.example.secretsantatelegrambot.controller.handler.impl;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.service.UserService;
import com.example.secretsantatelegrambot.util.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.io.Serializable;
import java.util.List;

import static com.example.secretsantatelegrambot.util.CommandUtil.CANCEL_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.HELP_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.YES_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.createFormattedCommand;
import static com.example.secretsantatelegrambot.util.CommandUtil.splitCommand;
import static com.example.secretsantatelegrambot.util.MessageUtil.HELP_MESSAGE;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createButton;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createEditMessageText;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createKeyboardWithButtonsInRow;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createSendMessage;

@Component
@RequiredArgsConstructor
public class HelpHandler implements Handler {
    private final UserService userService;
    private final Validator validator;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        Long chatId = user.getChatId();
        String[] args = splitCommand(message);

        boolean isNewMessage;
        if (validator.validateArguments(args, 2)) {
            isNewMessage = Boolean.parseBoolean(args[1]);
        }
        else {
            isNewMessage = true;
        }

        BotApiMethod helpMessage;
        if (isNewMessage) {
            helpMessage = createSendHelpMessage(chatId);
        }
        else {
            helpMessage = createEditHelpMessageText(chatId, messageId);
        }

        if (!UserState.START.equals(user.getUserState())) {
            userService.setUserState(user, UserState.START);
        }

        return List.of(helpMessage);
    }

    @Override
    public UserState operatedBotState() {
        return UserState.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(HELP_COMMAND);
    }

    private SendMessage createSendHelpMessage(Long chatId) {
        SendMessage helpMessage = createSendMessage(chatId, HELP_MESSAGE);
        helpMessage.setReplyMarkup(createKeyboardWithButtonsInRow(
                List.of(createButton(YES_BUTTON_MESSAGE,
                        createFormattedCommand(CANCEL_COMMAND, String.valueOf(false))))));
        return helpMessage;
    }

    private EditMessageText createEditHelpMessageText(Long chatId, Integer messageId) {
        EditMessageText helpMessage = createEditMessageText(chatId, messageId, HELP_MESSAGE);
        helpMessage.setReplyMarkup(createKeyboardWithButtonsInRow(
                List.of(createButton(YES_BUTTON_MESSAGE,
                        createFormattedCommand(CANCEL_COMMAND, String.valueOf(false))))));
        return helpMessage;
    }
}
