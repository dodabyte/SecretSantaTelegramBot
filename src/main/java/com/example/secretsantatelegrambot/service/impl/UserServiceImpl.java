package com.example.secretsantatelegrambot.service.impl;

import com.example.secretsantatelegrambot.entity.User;
import com.example.secretsantatelegrambot.entity.enums.UserState;
import com.example.secretsantatelegrambot.exception.impl.UserNotFoundException;
import com.example.secretsantatelegrambot.repository.UserRepository;
import com.example.secretsantatelegrambot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User findByChatId(Long chatId) {
        return userRepository.findByChatId(chatId).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User findByChatIdOrSaveUser(Long chatId, String username) {
        return userRepository.findByChatId(chatId)
                .orElseGet(() -> userRepository.save(User.builder()
                        .username(username)
                        .chatId(chatId)
                        .userState(UserState.START)
                        .registrationDate(LocalDateTime.now())
                        .build()));
    }

    @Override
    public void setUserState(User user, UserState userState) {
        if (!userState.equals(user.getUserState())) {
            user.setUserState(userState);
            userRepository.save(user);
        }
    }
}