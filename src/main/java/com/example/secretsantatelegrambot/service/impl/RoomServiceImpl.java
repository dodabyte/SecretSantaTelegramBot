package com.example.secretsantatelegrambot.service.impl;

import com.example.secretsantatelegrambot.entity.Room;
import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.exception.impl.RoomMinCostGiftFormatException;
import com.example.secretsantatelegrambot.exception.impl.RoomMaxCountUsersFormatException;
import com.example.secretsantatelegrambot.exception.impl.RoomMinCountUsersFormatException;
import com.example.secretsantatelegrambot.exception.impl.RoomNameFormatException;
import com.example.secretsantatelegrambot.exception.impl.RoomNotFoundException;
import com.example.secretsantatelegrambot.exception.impl.RoomReachedMaxCountUsersException;
import com.example.secretsantatelegrambot.exception.impl.UserAlreadyInRoomException;
import com.example.secretsantatelegrambot.exception.impl.UserNotAllowedException;
import com.example.secretsantatelegrambot.repository.RoomRepository;
import com.example.secretsantatelegrambot.repository.UserRepository;
import com.example.secretsantatelegrambot.service.GiftAssignmentService;
import com.example.secretsantatelegrambot.service.RoomService;
import com.example.secretsantatelegrambot.service.UserService;
import com.example.secretsantatelegrambot.util.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    private final UserService userService;
    private final GiftAssignmentService giftAssignmentService;

    private final Validator validator;

    @Override
    @Transactional
    public Set<Room> getAllUserRooms(Long chatId) {
        User user = userService.findByChatId(chatId);
        return user.getRooms().stream()
                .sorted(Comparator.comparing(Room::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Room getRoomByRoomId(UUID roomId, Long chatId) {
        return roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
    }

    @Override
    @Transactional
    public void createRoom(User oldUser, String name, int minCountUsers, int maxCountUsers, BigDecimal minCostGift) {
        Long chatId = oldUser.getChatId();

        validateAndThrow(name, minCountUsers, maxCountUsers, minCostGift);

        User user = userService.findByChatId(chatId);

        createRoomAndUpdateUser(user, name, minCountUsers, maxCountUsers, minCostGift);
    }

    @Override
    @Transactional
    public Room joinRoom(User oldUser, UUID roomId) {
        Long chatId = oldUser.getChatId();
        Room room = getRoomByRoomId(roomId, chatId);
        User user = userService.findByChatId(chatId);

        if (validator.validateUserInRoom(user, room)) {
            throw new UserAlreadyInRoomException();
        }

        if (!validator.validateMaxCountUsersInRoom(room)) {
            throw new RoomReachedMaxCountUsersException();
        }

        giftAssignmentService.clearAssignmentsInRoom(room);

        room.getUsers().add(user);
        if (user.getRooms() == null) {
            user.setRooms(new HashSet<>());
        }
        user.getRooms().add(room);

        userRepository.save(user);

        return room;
    }

    @Override
    @Transactional
    public Room updateRoom(User user, UUID roomId, String name, int minCountUsers, int maxCountUsers, BigDecimal minCostGift) {
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);

        if (!validator.validateAdminUserInRoom(room, user)) {
            throw new UserNotAllowedException();
        }

        validateAndSave(room, name, minCountUsers, maxCountUsers, minCostGift);

        return roomRepository.save(room);
    }

    @Override
    @Transactional
    public void deleteOrLeaveRoom(Long chatId, UUID roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        User user = userService.findByChatId(chatId);

        deleteOrLeave(room, user);
    }

    @Override
    @Transactional
    public void deleteOrLeaveRoom(String username, UUID roomId) {
        User user = userService.findByUsername(username);
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);

        deleteOrLeave(room, user);
    }

    private void deleteOrLeave(Room room, User user) {
        if (validator.validateAdminUserInRoom(room, user)) {
            Set<User> usersInRoom = room.getUsers();

            for (User userInRoom : usersInRoom) {
                userInRoom.getRooms().remove(room);
            }
            user.getAdminRooms().remove(room);
            room.getUsers().clear();

            giftAssignmentService.clearAssignmentsInRoom(room);
            roomRepository.delete(room);
            userRepository.saveAll(usersInRoom);
        }
        else {
            user.getRooms().remove(room);
            giftAssignmentService.clearAssignmentsInRoom(room);
            userRepository.save(user);
        }
    }

    private void createRoomAndUpdateUser(User user, String name, int minCountUsers, int maxCountUsers, BigDecimal minCostGift) {
        Set<User> users = new HashSet<>();
        users.add(user);

        Room room = Room.builder()
                .name(name)
                .minCountUsers(minCountUsers)
                .maxCountUsers(maxCountUsers)
                .minCostGift(minCostGift)
                .createdDate(LocalDateTime.now())
                .users(users)
                .adminUser(user)
                .build();

        Room savedRoom = roomRepository.save(room);

        user.getRooms().add(savedRoom);
        user.getAdminRooms().add(savedRoom);
    }

    private void validateAndThrow(String name, int minCountUsers, int maxCountUsers, BigDecimal minCostGift) {
        if (!validator.validateEnteredRoomNameFormat(name)) {
            throw new RoomNameFormatException();
        }
        if (!validator.validateEnteredRoomMinCountUsersFormat(minCountUsers)) {
            throw new RoomMinCountUsersFormatException();
        }
        if (!validator.validateEnteredRoomMaxCountUsersFormat(maxCountUsers, minCountUsers)) {
            throw new RoomMaxCountUsersFormatException();
        }
        if (!validator.validateEnteredMinCostGift(minCostGift)) {
            throw new RoomMinCostGiftFormatException();
        }
    }

    private void validateAndSave(Room room, String name, int minCountUsers, int maxCountUsers, BigDecimal minCostGift) {
        if (name != null && validator.validateEnteredRoomNameFormat(name)) {
            room.setName(name);
        }
        if (minCountUsers > 0 && validator.validateEnteredRoomMinCountUsersFormat(minCountUsers)) {
            room.setMinCountUsers(minCountUsers);
        }
        if (maxCountUsers > 0 && validator.validateEnteredRoomMaxCountUsersFormat(maxCountUsers, minCountUsers)) {
            room.setMaxCountUsers(maxCountUsers);
        }
        if (minCostGift != null && validator.validateEnteredMinCostGift(minCostGift)) {
            room.setMinCostGift(minCostGift);
        }
    }
}
