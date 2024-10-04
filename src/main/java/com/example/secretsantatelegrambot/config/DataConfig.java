package com.example.secretsantatelegrambot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class DataConfig {
    @Value("${options.room.min_length_name}")
    private int minLengthName;

    @Value("${options.room.max_length_name}")
    private int maxLengthName;

    @Value("${options.room.min_count_users}")
    private int minCountUsers;

    @Value("${options.room.max_count_users}")
    private int maxCountUsers;
}
