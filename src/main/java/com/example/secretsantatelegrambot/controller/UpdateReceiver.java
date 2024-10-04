package com.example.secretsantatelegrambot.controller;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.controller.handler.impl.CancelAndGetRoomDetailsHandler;
import com.example.secretsantatelegrambot.controller.handler.impl.CancelAndGetStartHandler;
import com.example.secretsantatelegrambot.controller.handler.impl.HelpHandler;
import com.example.secretsantatelegrambot.controller.handler.impl.StartHandler;
import com.example.secretsantatelegrambot.controller.handler.impl.join_room.StartAndJoinRoomHandler;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.exception.SecretSantaException;
import com.example.secretsantatelegrambot.service.UserService;
import com.example.secretsantatelegrambot.util.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static com.example.secretsantatelegrambot.util.CommandUtil.CANCEL_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.HELP_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.START_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.splitCommand;
import static com.example.secretsantatelegrambot.util.LogUtil.logException;
import static com.example.secretsantatelegrambot.util.MessageUtil.escape;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createSendMessage;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateReceiver {
    private final List<Handler> handlers;
    private final UserService userService;
    private final Validator validator;

    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        String username = "";
        String command = "";
        Long chatId = 0L;
        try {
            if (isMessageWithText(update)) {
                final Message message = update.getMessage();
                chatId = message.getFrom().getId();
                username = message.getFrom().getUserName();
                command = message.getText();

                User user = userService.findByChatIdOrSaveUser(chatId, username);

                return getHandlerByMessage(user.getUserState(), command)
                        .handle(user, message.getMessageId(), command);
            }
            else if (update.hasCallbackQuery()) {
                final CallbackQuery callbackQuery = update.getCallbackQuery();
                chatId = callbackQuery.getFrom().getId();
                username = callbackQuery.getFrom().getUserName();
                command = callbackQuery.getData();

                User user = userService.findByChatIdOrSaveUser(chatId, username);

                return getHandlerByQuery(user.getUserState(), command)
                        .handle(user, callbackQuery.getMessage().getMessageId(), command);
            }

            throw new UnsupportedOperationException();
        }
        catch (SecretSantaException e) {
            logException(LogLevel.WARN, e, username, command);
            return List.of(createSendMessage(chatId, escape(e.getMessage())));
        }
        catch (Exception e) {
            logException(LogLevel.ERROR, e, username, command);
            return Collections.emptyList();
        }
    }

    private Handler getHandlerByMessage(UserState state, String message) {
        if (message.startsWith(START_COMMAND)) {
            return getStartHandler(message);
        }
        else if (HELP_COMMAND.equals(message)) {
            return getHandlerByClass(HelpHandler.class);
        }
        else {
            return getHandlerByState(state);
        }
    }

    private Handler getHandlerByQuery(UserState state, String query) {
        if (query.startsWith(CANCEL_COMMAND)) {
            if (validator.validateArguments(splitCommand(query), 3)) {
                return getHandlerByClass(CancelAndGetRoomDetailsHandler.class);
            }
            else {
                return getHandlerByClass(CancelAndGetStartHandler.class);
            }
        }
        else if (query.startsWith(START_COMMAND)) {
            return getStartHandler(query);
        }
        else {
            return getHandlerByCallBackQuery(state, query);
        }
    }

    private Handler getStartHandler(String command) {
        if (validator.validateArguments(splitCommand(command), 2)) {
            return getHandlerByClass(StartAndJoinRoomHandler.class);
        }
        else {
            return getHandlerByClass(StartHandler.class);
        }
    }

    private Handler getHandlerByClass(Class clazz) {
        return handlers.stream()
                .filter(h -> h.getClass().equals(clazz))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private Handler getHandlerByState(UserState state) {
        return handlers.stream()
                .filter(h -> h.operatedBotState() != null)
                .filter(h -> h.operatedBotState().equals(state))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private Handler getHandlerByCallBackQuery(UserState state, String query) {
        return handlers.stream()
                .filter(h -> h.operatedBotState() != null)
                .filter(h -> h.operatedBotState().equals(state))
                .filter(h -> h.operatedCallBackQuery().stream()
                        .anyMatch(query::startsWith))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private boolean isMessageWithText(Update update) {
        return !update.hasCallbackQuery() && update.hasMessage() && update.getMessage().hasText();
    }
}
