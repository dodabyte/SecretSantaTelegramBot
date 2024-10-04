package com.example.secretsantatelegrambot.util;

import com.example.secretsantatelegrambot.entity.Room;
import com.example.secretsantatelegrambot.entity.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.secretsantatelegrambot.util.CommandUtil.BACK_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.CREATE_ROOM_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.CREATE_ROOM_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.DELETE_USER_FROM_ROOM_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.DELETE_USER_FROM_ROOM_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.FIND_RECEIVER_BY_GIVEN_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.FIND_RECEIVER_BY_GIVEN_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.GET_ALL_USERS_IN_ROOM_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.GET_ALL_USERS_IN_ROOM_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.GET_ALL_USER_ROOMS_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.GET_ALL_USER_ROOMS_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.HELP_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.HELP_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.LEAVE_ROOM_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.LEAVE_ROOM_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.RESTART_RANDOMIZATION_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.START_RANDOMIZATION_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.START_RANDOMIZATION_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.UPDATE_ROOM_BUTTON_MESSAGE;
import static com.example.secretsantatelegrambot.util.CommandUtil.UPDATE_ROOM_COMMAND;
import static com.example.secretsantatelegrambot.util.CommandUtil.createFormattedCommand;

public class TelegramUtil {
    public static SendMessage createSendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        sendMessage.setParseMode("MarkdownV2");
        return sendMessage;
    }

    public static EditMessageText createEditMessageText(Long chatId, Integer messageId, String message) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setText(message);
        editMessageText.setParseMode("MarkdownV2");
        return editMessageText;
    }

    public static EditMessageReplyMarkup deleteMessageReplyMarkup(Long chatId, Integer messageId) {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(chatId);
        editMessageReplyMarkup.setMessageId(messageId);
        editMessageReplyMarkup.setReplyMarkup(null);
        return editMessageReplyMarkup;
    }

    public static InlineKeyboardButton createButton(String text, String command) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallbackData(command);
        return inlineKeyboardButton;
    }

    public static InlineKeyboardMarkup createKeyboardWithButtonsInRow(List<InlineKeyboardButton> buttons) {
        return createKeyboardWithButtonsInColumn(List.of(buttons));
    }

    public static InlineKeyboardMarkup createKeyboardWithButtonsInColumn(List<List<InlineKeyboardButton>> buttons) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.setKeyboard(buttons);

        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup createInlineKeyboardWithMainButtons() {
        return createKeyboardWithButtonsInColumn(List.of(
                List.of(
                        createButton(GET_ALL_USER_ROOMS_BUTTON_MESSAGE, GET_ALL_USER_ROOMS_COMMAND),
                        createButton(CREATE_ROOM_BUTTON_MESSAGE, CREATE_ROOM_COMMAND)
                ),
                List.of(
                        createButton(HELP_BUTTON_MESSAGE, createFormattedCommand(HELP_COMMAND, String.valueOf(false)))
                )
            )
        );
    }

    public static InlineKeyboardMarkup createKeyboardWithRoomDetails(Room room, User user, boolean existsGiftAssignment) {
        String roomId = room.getId().toString();

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>(List.of(
                List.of(createButton(GET_ALL_USERS_IN_ROOM_BUTTON_MESSAGE,
                        createFormattedCommand(GET_ALL_USERS_IN_ROOM_COMMAND, roomId)))
        ));

        if (existsGiftAssignment) {
            addButtonsIfGiftAssignment(buttons, room.getId());
        }

        addButtonsIfUserIsAdmin(buttons, room, user, existsGiftAssignment);

        buttons.addAll(List.of(
                List.of(createButton(LEAVE_ROOM_BUTTON_MESSAGE,
                        createFormattedCommand(LEAVE_ROOM_COMMAND, roomId))),
                List.of(createButton(BACK_BUTTON_MESSAGE, GET_ALL_USER_ROOMS_COMMAND))
        ));

        return createKeyboardWithButtonsInColumn(buttons);
    }

    private static void addButtonsIfGiftAssignment(List<List<InlineKeyboardButton>> buttons, UUID roomId) {
        buttons.add(
                List.of(createButton(FIND_RECEIVER_BY_GIVEN_BUTTON_MESSAGE,
                        createFormattedCommand(FIND_RECEIVER_BY_GIVEN_COMMAND, roomId.toString())))
        );
    }

    private static void addButtonsIfUserIsAdmin(List<List<InlineKeyboardButton>> buttons, Room room, User user, boolean existsGiftAssignment) {
        String roomId = room.getId().toString();
        if (room.getAdminUser().getId().equals(user.getId())) {
            String randomizationMessage = existsGiftAssignment ? RESTART_RANDOMIZATION_BUTTON_MESSAGE : START_RANDOMIZATION_BUTTON_MESSAGE;
            buttons.addAll(List.of(
                    List.of(createButton(randomizationMessage,
                            createFormattedCommand(START_RANDOMIZATION_COMMAND, roomId))),
                    List.of(createButton(UPDATE_ROOM_BUTTON_MESSAGE,
                            createFormattedCommand(UPDATE_ROOM_COMMAND, roomId))),
                    List.of(createButton(DELETE_USER_FROM_ROOM_BUTTON_MESSAGE,
                            createFormattedCommand(DELETE_USER_FROM_ROOM_COMMAND, roomId)))
            ));
        }
    }
}
