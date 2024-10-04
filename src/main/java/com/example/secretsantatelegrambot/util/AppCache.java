package com.example.secretsantatelegrambot.util;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AppCache {
    // Key - userId, Value - list of entered count users in room (min and max)
    public static final Map<UUID, List<Integer>> ENTERED_COUNTS_USERS_IN_ROOM_CACHE = new ConcurrentHashMap<>();
    // Key - userId, Value - entered name room
    public static final Map<UUID, String> ENTERED_NAME_ROOM_CACHE = new ConcurrentHashMap<>();

    // Key - userId, Value - entered username
    public static final Map<UUID, String> ENTERED_USERNAME_CACHE = new ConcurrentHashMap<>();

    // Key - userId, Value - roomId
    public static final Map<UUID, UUID> ROOM_ID_CACHE = new ConcurrentHashMap<>();

    public static final Map<UUID, Integer> MESSAGE_ID_CACHE = new ConcurrentHashMap<>();

    public static void checkAndRemoveUserCache(UUID userId) {
        ENTERED_COUNTS_USERS_IN_ROOM_CACHE.remove(userId);
        ENTERED_NAME_ROOM_CACHE.remove(userId);
        ENTERED_USERNAME_CACHE.remove(userId);
        ROOM_ID_CACHE.remove(userId);
    }

    public static Integer getMessageIdFromCache(UUID userId) {
        Integer messageId = -1;
        if (MESSAGE_ID_CACHE.containsKey(userId)) {
            messageId = MESSAGE_ID_CACHE.get(userId);
            MESSAGE_ID_CACHE.remove(userId);
        }
        return messageId;
    }
}
