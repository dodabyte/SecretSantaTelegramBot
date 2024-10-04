package com.example.secretsantatelegrambot.service.impl;

import com.example.secretsantatelegrambot.entity.GiftAssignment;
import com.example.secretsantatelegrambot.entity.Room;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.exception.impl.CountUsersLessMinCountUsersException;
import com.example.secretsantatelegrambot.service.GiftAssignmentService;
import com.example.secretsantatelegrambot.service.RandomService;
import com.example.secretsantatelegrambot.service.RoomService;
import com.example.secretsantatelegrambot.util.Validator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RandomServiceImpl implements RandomService {
    private final RoomService roomService;
    private final GiftAssignmentService giftAssignmentService;

    private final Validator validator;

    @Override
    @Transactional
    public List<GiftAssignment> startRandomization(UUID roomId, Long chatId) {
        Room room = roomService.getRoomByRoomId(roomId, chatId);

        if (!validator.validateCountUsersMoreMinCountUsersInRoom(room)) {
            throw new CountUsersLessMinCountUsersException();
        }

        giftAssignmentService.clearAssignmentsInRoom(room);

        List<User> givenUsers = new ArrayList<>(room.getUsers());
        List<User> receivingUsers = createRandomReceivingUsers(givenUsers);

        return giftAssignmentService.assignGifts(givenUsers, receivingUsers, room);
    }

    private List<User> createRandomReceivingUsers(List<User> givenUsers) {
        List<User> receivingUsers = new ArrayList<>(givenUsers);
        Collections.shuffle(receivingUsers);

        for (int i = 0; i < givenUsers.size(); i++) {
            if (givenUsers.get(i) == receivingUsers.get(i)) {
                User receiver;
                if (i + 1 < givenUsers.size()){
                    receiver = receivingUsers.get(i + 1);
                    receivingUsers.set(i + 1, receivingUsers.get(i));
                }
                else {
                    receiver = receivingUsers.get(1);
                    receivingUsers.set(1, receivingUsers.get(i));
                }
                receivingUsers.set(i , receiver);
            }
        }

        return receivingUsers;
    }
}
