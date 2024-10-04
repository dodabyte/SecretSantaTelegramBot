package com.example.secretsantatelegrambot.controller.handler.impl.delete_user_from_room;

import com.example.secretsantatelegrambot.controller.handler.Handler;
import com.example.secretsantatelegrambot.entity.Room;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.service.GiftAssignmentService;
import com.example.secretsantatelegrambot.service.RoomService;
import com.example.secretsantatelegrambot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import static com.example.secretsantatelegrambot.util.AppCache.ENTERED_USERNAME_CACHE;
import static com.example.secretsantatelegrambot.util.AppCache.checkAndRemoveUserCache;
import static com.example.secretsantatelegrambot.util.AppCache.getMessageIdFromCache;
import static com.example.secretsantatelegrambot.util.CommandUtil.DELETE_USER_FROM_ROOM_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.getRoomIdFromCommand;
import static com.example.secretsantatelegrambot.util.MessageUtil.createRoomDetailsMessage;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createEditMessageText;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createKeyboardWithRoomDetails;
import static com.example.secretsantatelegrambot.util.TelegramUtil.deleteMessageReplyMarkup;

@Component
@RequiredArgsConstructor
public class DeleteUserFromRoomConfirmHandler implements Handler {
    private final RoomService roomService;
    private final UserService userService;
    private final GiftAssignmentService giftAssignmentService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, Integer messageId, String message) {
        Long chatId = user.getChatId();
        UUID userId = user.getId();
        UUID roomId = getRoomIdFromCommand(chatId, message);
        String enteredUsername = ENTERED_USERNAME_CACHE.get(userId);

        Room room = roomService.getRoomByRoomId(roomId, chatId);
        boolean existsGiftAssignment = giftAssignmentService.existsByRoomId(roomId);

        roomService.deleteOrLeaveRoom(enteredUsername, roomId);

        EditMessageReplyMarkup editPreviousMessage = deleteMessageReplyMarkup(chatId, getMessageIdFromCache(userId));

        EditMessageText deleteUserFromRoomMessage = createEditMessageText(chatId, messageId,
                createRoomDetailsMessage(room));
        deleteUserFromRoomMessage.setReplyMarkup(createKeyboardWithRoomDetails(room, user, existsGiftAssignment));

        checkAndRemoveUserCache(userId);

        userService.setUserState(user, UserState.START);

        return List.of(editPreviousMessage, deleteUserFromRoomMessage);
    }

    @Override
    public UserState operatedBotState() {
        return UserState.AWAITING_DELETE_CONFIRM;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(DELETE_USER_FROM_ROOM_COMMAND);
    }
}
