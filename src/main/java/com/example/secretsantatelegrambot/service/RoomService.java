package com.example.secretsantatelegrambot.service;

import com.example.secretsantatelegrambot.entity.Room;
import com.example.secretsantatelegrambot.entity.User;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public interface RoomService {
    Set<Room> getAllUserRooms(Long chatId);

    Room getRoomByRoomId(UUID roomId, Long chatId);

    void createRoom(User user, String name, int minCountUsers, int maxCountUsers, BigDecimal minCostGift);

    Room joinRoom(User user, UUID roomId);

    Room updateRoom(User user, UUID roomId, String name, int minCountUsers, int maxCountUsers, BigDecimal minCostGift);

    void deleteOrLeaveRoom(Long chatId, UUID roomId);

    void deleteOrLeaveRoom(String username, UUID roomId);
}
