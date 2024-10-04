package com.example.secretsantatelegrambot.util;

import com.example.secretsantatelegrambot.exception.impl.RoomIdFormatException;

import java.util.UUID;

public class CommandUtil {
    public static final String HELP_BUTTON_MESSAGE = "\uD83D\uDCF0 Подробности";
    public static final String CREATE_ROOM_BUTTON_MESSAGE = "\uD83C\uDD95 Создать комнату";
    public static final String UPDATE_ROOM_BUTTON_MESSAGE = "\uD83D\uDCDD Редактировать комнату";
    public static final String LEAVE_ROOM_BUTTON_MESSAGE = "❌ Выйти из комнаты";
    public static final String GET_ALL_USER_ROOMS_BUTTON_MESSAGE = "\uD83D\uDCCB Мои комнаты";
    public static final String START_BUTTON_MESSAGE = "▶️ Начать";
    public static final String START_RANDOMIZATION_BUTTON_MESSAGE = "\uD83E\uDE84 Начать рандомизацию";
    public static final String RESTART_RANDOMIZATION_BUTTON_MESSAGE = "\uD83D\uDD04 Перезапустить рандомизацию";
    public static final String FIND_RECEIVER_BY_GIVEN_BUTTON_MESSAGE = "\uD83C\uDF81 Кому я дарю?";
    public static final String DELETE_USER_FROM_ROOM_BUTTON_MESSAGE = "\uD83C\uDFCC\uD83C\uDFFB Выгнать участника";
    public static final String GET_ALL_USERS_IN_ROOM_BUTTON_MESSAGE = "\uD83D\uDCCB Список участников";
    public static final String YES_BUTTON_MESSAGE = "✅";
    public static final String NO_BUTTON_MESSAGE = "❌";
    public static final String CANCEL_BUTTON_MESSAGE = "⬅️ Отмена";
    public static final String WAIT_OTHER_USERS_BUTTON_MESSAGE = "⏳ Подождать";
    public static final String BACK_BUTTON_MESSAGE = "⬅️ Назад";
    public static final String UPDATE_ROOM_NAME_BUTTON_MESSAGE = "Название";
    public static final String UPDATE_ROOM_MIN_COUNT_USERS_BUTTON_MESSAGE = "Мин. кол-во участников";
    public static final String UPDATE_ROOM_MAX_COUNT_USERS_BUTTON_MESSAGE = "Макс. кол-во участников";
    public static final String UPDATE_ROOM_MIN_COST_GIFT_BUTTON_MESSAGE = "Мин. стоимость подарков";

    public static final String START_COMMAND = "/start";
    public static final String HELP_COMMAND = "/help";
    public static final String CREATE_ROOM_COMMAND = "/createRoom";
    public static final String UPDATE_ROOM_COMMAND = "/updateRoom";
    public static final String UPDATE_ROOM_NAME_COMMAND = "/updateRoomName";
    public static final String UPDATE_ROOM_MIN_COUNT_USERS_COMMAND = "/updateRoomMinCountUsers";
    public static final String UPDATE_ROOM_MAX_COUNT_USERS_COMMAND = "/updateRoomMaxCountUsers";
    public static final String UPDATE_ROOM_MIN_COST_GIFT_COMMAND = "/updateRoomMinCostGift";
    public static final String LEAVE_ROOM_COMMAND = "/leaveRoom";
    public static final String GET_ALL_USER_ROOMS_COMMAND = "/myRooms";
    public static final String ROOM_DETAILS_COMMAND = "/roomDetails";
    public static final String START_RANDOMIZATION_COMMAND = "/randomizationStart";
    public static final String CONFIRM_RANDOMIZATION_COMMAND = "/randomizationConfirm";
    public static final String DELETE_USER_FROM_ROOM_COMMAND = "/deleteUserFromRoom";
    public static final String GET_ALL_USERS_IN_ROOM_COMMAND = "/usersInRoom";
    public static final String FIND_RECEIVER_BY_GIVEN_COMMAND = "/findReceiverByGiven";
    public static final String CANCEL_COMMAND = "/cancel";

    public static String createFormattedCommand(String command, String... parameters) {
        StringBuilder builder = new StringBuilder();
        builder.append(command);
        for (String parameter : parameters) {
            builder.append(" ").append(parameter);
        }
        return builder.toString();
    }

    public static String[] splitCommand(String command) {
        return command.split(" ");
    }

    public static UUID getRoomIdFromCommand(Long chatId, String message) {
        String[] args = splitCommand(message);

        if (args.length < 2) {
            throw new IllegalArgumentException();
        }

        return getRoomIdFromText(args[1]);
    }

    public static UUID getRoomIdFromText(String text) {
        UUID roomId;
        try {
            roomId = UUID.fromString(text);
        }
        catch (IllegalArgumentException e) {
            throw new RoomIdFormatException();
        }
        return roomId;
    }

    public static String getUsernameFromText(String text) {
        return text.startsWith("@") ? text.substring(1) : text;
    }
}
