package com.example.secretsantatelegrambot.controller.handler.impl.update_room;

import com.example.secretsantatelegrambot.entity.Room;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.service.GiftAssignmentService;
import com.example.secretsantatelegrambot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import static com.example.secretsantatelegrambot.util.AppCache.checkAndRemoveUserCache;
import static com.example.secretsantatelegrambot.util.AppCache.getMessageIdFromCache;
import static com.example.secretsantatelegrambot.util.MessageUtil.createRoomDetailsMessage;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createKeyboardWithRoomDetails;
import static com.example.secretsantatelegrambot.util.TelegramUtil.createSendMessage;
import static com.example.secretsantatelegrambot.util.TelegramUtil.deleteMessageReplyMarkup;

@Component
@RequiredArgsConstructor
public class UpdateRoomGetGlobalHandler {
    private final GiftAssignmentService giftAssignmentService;
    private final UserService userService;

    public List<PartialBotApiMethod<? extends Serializable>> getPartialBotApiMethods(Long chatId, UUID roomId, Room room) {
        User user = userService.findByChatId(chatId);
        boolean existsGiftAssignment = giftAssignmentService.existsByRoomId(roomId);

        EditMessageReplyMarkup editPreviousMessage = deleteMessageReplyMarkup(chatId, getMessageIdFromCache(user.getId()));

        SendMessage updateRoomMessage = createSendMessage(chatId, createRoomDetailsMessage(room));
        updateRoomMessage.setReplyMarkup(createKeyboardWithRoomDetails(room, user, existsGiftAssignment));

        checkAndRemoveUserCache(user.getId());

        userService.setUserState(user, UserState.START);

        return List.of(editPreviousMessage, updateRoomMessage);
    }
}
