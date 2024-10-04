package com.example.secretsantatelegrambot.controller.handler.impl.update_room;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.entity.Room;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.exception.impl.RoomMaxCountUsersFormatException;
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
public class UpdateRoomGetMaxCountUsersHandler implements Handler {
    private final RoomService roomService;
    private final UpdateRoomGetGlobalHandler updateHandler;
    private final Validator validator;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        Long chatId = user.getChatId();
        UUID roomId = ROOM_ID_CACHE.get(user.getId());
        Room room = roomService.getRoomByRoomId(roomId, chatId);
        int maxCountUsers = getMaxCountUsers(room.getMinCountUsers(), message);

        Room updatedRoom = roomService.updateRoom(user, roomId, null, 0, maxCountUsers, null);

        return updateHandler.getPartialBotApiMethods(chatId, roomId, updatedRoom);
    }

    @Override
    public UserState operatedBotState() {
        return UserState.AWAITING_UPDATE_MAX_COUNT_USERS_ROOM;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }

    private int getMaxCountUsers(int currentMinCountUsers, String message) {
        int maxCountUsers;

        try {
            maxCountUsers = Integer.parseInt(message);
        }
        catch (NumberFormatException e) {
            throw new RoomMaxCountUsersFormatException();
        }

        if (!validator.validateEnteredRoomMaxCountUsersFormat(maxCountUsers, currentMinCountUsers)) {
            throw new RoomMaxCountUsersFormatException();
        }

        return maxCountUsers;
    }
}