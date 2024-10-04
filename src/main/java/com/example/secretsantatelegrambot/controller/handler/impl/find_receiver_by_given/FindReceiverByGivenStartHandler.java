package com.example.secretsantatelegrambot.controller.handler.impl.find_receiver_by_given;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.entity.Room;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.service.GiftAssignmentService;
import com.example.secretsantatelegrambot.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import static com.example.secretsantatelegrambot.util.CommandUtil.BACK_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.CANCEL_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.FIND_RECEIVER_BY_GIVEN_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.createFormattedCommand;
import static com.example.secretsantatelegrambot.util.CommandUtil.getRoomIdFromCommand;
import static com.example.secretsantatelegrambot.util.MessageUtil.FIND_RECEIVER_BY_GIVEN_MESSAGE;
import static com.example.secretsantatelegrambot.util.MessageUtil.escape;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createButton;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createEditMessageText;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createKeyboardWithButtonsInRow;

@Component
@RequiredArgsConstructor
public class FindReceiverByGivenStartHandler implements Handler {
    private final RoomService roomService;
    private final GiftAssignmentService giftAssignmentService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        try {
            Long chatId = user.getChatId();
            UUID roomId = getRoomIdFromCommand(chatId, message);
            Room room = roomService.getRoomByRoomId(roomId, chatId);

            User receiver = giftAssignmentService.getReceiverByGiver(user, room);

            EditMessageText findReceiverMessage = createEditMessageText(chatId, messageId,
                    FIND_RECEIVER_BY_GIVEN_MESSAGE.formatted(escape(receiver.getUsername())));
            findReceiverMessage.setReplyMarkup(createKeyboardWithButtonsInRow(
                    List.of(createButton(BACK_BUTTON_MESSAGE,
                            createFormattedCommand(CANCEL_COMMAND, roomId.toString(), String.valueOf(false))))));

            return List.of(findReceiverMessage);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return List.of();
        }
    }

    @Override
    public UserState operatedBotState() {
        return UserState.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(FIND_RECEIVER_BY_GIVEN_COMMAND);
    }
}
