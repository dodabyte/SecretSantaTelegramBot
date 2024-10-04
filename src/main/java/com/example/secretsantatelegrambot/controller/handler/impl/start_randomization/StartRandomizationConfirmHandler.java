package com.example.secretsantatelegrambot.controller.handler.impl.start_randomization;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.entity.GiftAssignment;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.service.RandomService;
import com.example.secretsantatelegrambot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.secretsantatelegrambot.util.CommandUtil.CONFIRM_RANDOMIZATION_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.START_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.YES_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.getRoomIdFromCommand;
import static com.example.secretsantatelegrambot.util.MessageUtil.AFTER_RANDOMIZATION_MESSAGE;
import static com.example.secretsantatelegrambot.util.MessageUtil.escape;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createButton;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createKeyboardWithButtonsInRow;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createSendMessage;
import static com.example.secretsantatelegrambot.util.TelegramUtil.deleteMessageReplyMarkup;

@Component
@RequiredArgsConstructor
public class StartRandomizationConfirmHandler implements Handler {
    private final UserService userService;
    private final RandomService randomService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        Long chatId = user.getChatId();
        UUID roomId = getRoomIdFromCommand(user.getChatId(), message);

        List<GiftAssignment> giftAssignments = randomService.startRandomization(roomId, chatId);

        List<PartialBotApiMethod<? extends Serializable>> messages = new ArrayList<>();

        EditMessageReplyMarkup editPreviousMessage = deleteMessageReplyMarkup(chatId, messageId);
        messages.add(editPreviousMessage);

        for (GiftAssignment giftAssignment : giftAssignments) {
            Long giverChatId = giftAssignment.getGiver().getChatId();
            String receiverUsername = giftAssignment.getReceiver().getUsername();
            String roomName = giftAssignment.getRoom().getName();

            SendMessage startRandomizationMessage = createSendMessage(giverChatId,
                    AFTER_RANDOMIZATION_MESSAGE.formatted(escape(roomName), escape(receiverUsername)));
            startRandomizationMessage.setReplyMarkup(createKeyboardWithButtonsInRow(
                    List.of(createButton(YES_BUTTON_MESSAGE, START_COMMAND))));

            userService.setUserState(giftAssignment.getGiver(), UserState.START);

            messages.add(startRandomizationMessage);
        }

        return messages;
    }

    @Override
    public UserState operatedBotState() {
        return UserState.AWAITING_START_RANDOMIZATION;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(CONFIRM_RANDOMIZATION_COMMAND);
    }
}
