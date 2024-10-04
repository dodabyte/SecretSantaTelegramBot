package com.example.secretsantatelegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
//@EnableCaching
public class SecretSantaTelegramBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecretSantaTelegramBotApplication.class, args);
    }
}
