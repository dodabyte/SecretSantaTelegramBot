package com.example.secretsantatelegrambot.controller.handler.impl.room_details;

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

import static com.example.secretsantatelegrambot.util.CommandUtil.ROOM_DETAILS_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.getRoomIdFromCommand;
import static com.example.secretsantatelegrambot.util.MessageUtil.createRoomDetailsMessage;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createEditMessageText;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createKeyboardWithRoomDetails;

@Component
@RequiredArgsConstructor
public class RoomDetailsStartHandler implements Handler {
    private final RoomService roomService;
    private final GiftAssignmentService giftAssignmentService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        Long chatId = user.getChatId();
        UUID roomId = getRoomIdFromCommand(chatId, message);

        Room room = roomService.getRoomByRoomId(roomId, chatId);
        boolean existsGiftAssignment = giftAssignmentService.existsByRoomId(roomId);

        EditMessageText listRoomsMessage = createEditMessageText(chatId, messageId,
                createRoomDetailsMessage(room));
        listRoomsMessage.setReplyMarkup(createKeyboardWithRoomDetails(room, user, existsGiftAssignment));

        return List.of(listRoomsMessage);
    }

    @Override
    public UserState operatedBotState() {
        return UserState.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(ROOM_DETAILS_COMMAND);
    }
}
