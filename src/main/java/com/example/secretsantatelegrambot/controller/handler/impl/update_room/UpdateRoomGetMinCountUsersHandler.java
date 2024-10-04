package com.example.secretsantatelegrambot.controller.handler.impl.update_room;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.entity.Room;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.exception.impl.RoomMinCountUsersFormatException;
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
public class UpdateRoomGetMinCountUsersHandler implements Handler {
    private final RoomService roomService;
    private final UpdateRoomGetGlobalHandler updateHandler;
    private final Validator validator;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        UUID roomId = ROOM_ID_CACHE.get(user.getId());
        int minCountUsers = getMinCountUsers(message);

        Room room = roomService.updateRoom(user, roomId, null, minCountUsers, 0, null);

        return updateHandler.getPartialBotApiMethods(user.getChatId(), roomId, room);
    }

    @Override
    public UserState operatedBotState() {
        return UserState.AWAITING_UPDATE_MIN_COUNT_USERS_ROOM;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }

    private int getMinCountUsers(String message) {
        int minCountUsers;

        try {
            minCountUsers = Integer.parseInt(message);
        }
        catch (NumberFormatException e) {
            throw new RoomMinCountUsersFormatException();
        }

        if (!validator.validateEnteredRoomMinCountUsersFormat(minCountUsers)) {
            throw new RoomMinCountUsersFormatException();
        }

        return minCountUsers;
    }
}