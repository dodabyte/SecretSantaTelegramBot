package com.example.secretsantatelegrambot.controller.handler.impl.update_room;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.entity.Room;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.exception.impl.RoomNameFormatException;
import com.example.secretsantatelegrambot.service.RoomService;
import com.example.secretsantatelegrambot.util.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.example.secretsantatelegrambot.util.AppCache.ROOM_ID_CACHE;

@Component
@RequiredArgsConstructor
public class UpdateRoomGetNameHandler implements Handler {
    private final RoomService roomService;
    private final UpdateRoomGetGlobalHandler updateHandler;
    private final Validator validator;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        if (!validator.validateEnteredRoomNameFormat(message)) {
            throw new RoomNameFormatException();
        }

        UUID roomId = ROOM_ID_CACHE.get(user.getId());

        Room room = roomService.updateRoom(user, roomId, message, 0, 0, null);

        return updateHandler.getPartialBotApiMethods(user.getChatId(), roomId, room);
    }

    @Override
    public UserState operatedBotState() {
        return UserState.AWAITING_UPDATE_NAME_ROOM;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }
}