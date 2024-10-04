package com.example.secretsantatelegrambot.service.impl;

import com.example.secretsantatelegrambot.entity.GiftAssignment;
import com.example.secretsantatelegrambot.entity.Room;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.exception.impl.GiftAssignmentNotFoundException;
import com.example.secretsantatelegrambot.repository.GiftAssignmentRepository;
import com.example.secretsantatelegrambot.service.GiftAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GiftAssignmentServiceImpl implements GiftAssignmentService {
    private final GiftAssignmentRepository giftAssignmentRepository;

    @Override
    public User getReceiverByGiver(User giver, Room room) {
        GiftAssignment giftAssignment = giftAssignmentRepository
                .findByGiverIdAndRoomId(giver.getId(), room.getId())
                .orElseThrow(GiftAssignmentNotFoundException::new);
        return giftAssignment.getReceiver();
    }

    @Override
    public List<GiftAssignment> assignGifts(List<User> giverUsers, List<User> receiverUsers, Room room) {
        List<GiftAssignment> giftAssignments = new ArrayList<>();

        for (int i = 0; i < giverUsers.size(); i++) {
            giftAssignments.add(GiftAssignment.builder()
                    .giver(giverUsers.get(i))
                    .receiver(receiverUsers.get(i))
                    .room(room)
                    .build());
        }

        return giftAssignmentRepository.saveAll(giftAssignments);
    }

    @Override
    public void clearAssignmentsInRoom(Room room) {
        giftAssignmentRepository.deleteAllByRoomId(room.getId());
    }

    @Override
    public boolean existsByRoomId(UUID roomId) {
        return giftAssignmentRepository.existsByRoomId(roomId);
    }
}
