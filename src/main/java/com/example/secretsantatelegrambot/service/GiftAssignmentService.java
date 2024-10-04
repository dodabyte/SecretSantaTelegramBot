package com.example.secretsantatelegrambot.service;

import com.example.secretsantatelegrambot.entity.GiftAssignment;
import com.example.secretsantatelegrambot.entity.Room;
import com.example.secretsantatelegrambot.entity.User;

import java.util.List;
import java.util.UUID;

public interface GiftAssignmentService {
    User getReceiverByGiver(User giver, Room room);

    List<GiftAssignment> assignGifts(List<User> giverUsers, List<User> receiverUsers, Room room);

    void clearAssignmentsInRoom(Room room);

    boolean existsByRoomId(UUID roomId);
}