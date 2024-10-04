package com.example.secretsantatelegrambot.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;

import static com.example.secretsantatelegrambot.util.MessageUtil.EXCEPTION_MESSAGE;

@Slf4j
public class LogUtil {
    public static void logException(LogLevel logLevel, Exception e, String username, String command) {
        switch (logLevel) {
            case WARN -> log.warn(EXCEPTION_MESSAGE, e.getStackTrace()[0].getClassName(), username, command,
                    e.getClass().getSimpleName(), e.getMessage());
            case ERROR -> log.error(EXCEPTION_MESSAGE, e.getStackTrace()[0].getClassName(), username, command,
                    e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
