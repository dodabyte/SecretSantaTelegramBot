package com.example.secretsantatelegrambot.controller.handler.impl.update_room;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.entity.Room;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import static com.example.secretsantatelegrambot.util.CommandUtil.UPDATE_ROOM_MIN_COST_GIFT_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.getRoomIdFromCommand;
import static com.example.secretsantatelegrambot.util.MessageUtil.UPDATE_ROOM_WITH_MIN_COST_GIFT_MESSAGE;
import static com.example.secretsantatelegrambot.util.MessageUtil.escape;

@Component
@RequiredArgsConstructor
public class UpdateRoomAwaitingMinCostGiftHandler implements Handler {
    private final RoomService roomService;
    private final UpdateRoomAwaitingGlobalHandler updateHandler;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        Long chatId = user.getChatId();
        UUID roomId = getRoomIdFromCommand(chatId, message);
        Room room = roomService.getRoomByRoomId(roomId, chatId);

        return updateHandler.getPartialBotApiMethods(user, messageId,
                UPDATE_ROOM_WITH_MIN_COST_GIFT_MESSAGE.formatted(escape(room.getMinCostGift().toString())),
                UserState.AWAITING_UPDATE_MIN_COST_GIFT_ROOM, roomId);
    }

    @Override
    public UserState operatedBotState() {
        return UserState.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(UPDATE_ROOM_MIN_COST_GIFT_COMMAND);
    }
}