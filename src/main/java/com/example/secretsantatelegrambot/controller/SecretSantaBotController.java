package com.example.secretsantatelegrambot.controller;

import com.example.secretsantatelegrambot.config.TelegramConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SecretSantaBotController")
public class SecretSantaBotController extends TelegramLongPollingBot {
    private final TelegramConfig telegramConfig;
    private final UpdateReceiver updateReceiver;

    @Override
    public void onUpdateReceived(Update update) {
        List<PartialBotApiMethod<? extends Serializable>> messagesToSend = updateReceiver.handle(update);

        if (messagesToSend != null && !messagesToSend.isEmpty()) {
            messagesToSend.forEach(response -> {
                if (response instanceof BotApiMethod) {
                    executeWithExceptionCheck((BotApiMethod) response);
                }
            });
        }
    }

    @Override
    public String getBotUsername() {
        return telegramConfig.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return telegramConfig.getBotToken();
    }

    private void executeWithExceptionCheck(BotApiMethod message) {
        try {
            execute(message);
        }
        catch (TelegramApiException e) {
            log.error("Исключение {}. Сообщение: {}", e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
