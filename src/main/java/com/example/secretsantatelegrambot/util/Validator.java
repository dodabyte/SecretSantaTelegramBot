package com.example.secretsantatelegrambot.util;

import com.example.secretsantatelegrambot.config.DataConfig;
import com.example.secretsantatelegrambot.entity.Room;
import com.example.secretsantatelegrambot.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class Validator {
    private final DataConfig dataConfig;

    public boolean validateEnteredRoomNameFormat(String name) {
        return !name.isEmpty() && name.length() >= dataConfig.getMinLengthName()
                && name.length() <= dataConfig.getMaxLengthName();
    }

    public boolean validateEnteredRoomMaxCountUsersFormat(int maxCountUsers, int minCountUsers) {
        return maxCountUsers <= dataConfig.getMaxCountUsers() && maxCountUsers >= minCountUsers;
    }

    public boolean validateEnteredRoomMinCountUsersFormat(int minCountUsers) {
        return minCountUsers >= dataConfig.getMinCountUsers();
    }

    public boolean validateEnteredMinCostGift(BigDecimal minCostGift) {
        return minCostGift.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean validateAdminUserInRoom(Room room, User user){
        return room.getAdminUser().getId().equals(user.getId());
    }

    public boolean validateMaxCountUsersInRoom(Room room) {
        return room.getUsers().size() < room.getMaxCountUsers();
    }

    public boolean validateMinCountUsersInRoom(Room room) {
        return room.getUsers().size() == room.getMinCountUsers();
    }

    public boolean validateCountUsersMoreMinCountUsersInRoom(Room room) {
        return room.getUsers().size() >= room.getMinCountUsers();
    }

    public boolean validateUserInRoom(User user, Room room) {
        return room.getUsers().stream()
                .map(User::getId)
                .anyMatch(id -> id.equals(user.getId()));
    }

    public boolean validateArguments(String[] args, int trueCountArgs) {
        return args.length >= trueCountArgs;
    }
}
