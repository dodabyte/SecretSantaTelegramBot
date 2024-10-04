package com.example.secretsantatelegrambot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class TelegramConfig {
    @Value("${bot.username}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;
}
