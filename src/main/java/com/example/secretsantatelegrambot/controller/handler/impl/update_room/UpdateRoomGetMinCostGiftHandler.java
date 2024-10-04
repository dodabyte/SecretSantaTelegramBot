package com.example.secretsantatelegrambot.controller.handler.impl.update_room;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.entity.Room;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.exception.impl.RoomMinCostGiftFormatException;
import com.example.secretsantatelegrambot.service.RoomService;
import com.example.secretsantatelegrambot.util.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.example.secretsantatelegrambot.util.AppCache.ROOM_ID_CACHE;

@Component
@RequiredArgsConstructor
public class UpdateRoomGetMinCostGiftHandler implements Handler {
    private final RoomService roomService;
    private final UpdateRoomGetGlobalHandler updateHandler;
    private final Validator validator;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        UUID roomId = ROOM_ID_CACHE.get(user.getId());
        BigDecimal minCostGift = getMinCostGift(message);

        Room room = roomService.updateRoom(user, roomId, null, 0, 0, minCostGift);

        return updateHandler.getPartialBotApiMethods(user.getChatId(), roomId, room);
    }

    @Override
    public UserState operatedBotState() {
        return UserState.AWAITING_UPDATE_MIN_COST_GIFT_ROOM;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }

    private BigDecimal getMinCostGift(String message) {
        BigDecimal minCostGift;

        try {
            minCostGift = new BigDecimal(message);
        }
        catch (NumberFormatException e) {
            throw new RoomMinCostGiftFormatException();
        }

        if (!validator.validateEnteredMinCostGift(minCostGift)) {
            throw new RoomMinCostGiftFormatException();
        }

        return minCostGift;
    }
}