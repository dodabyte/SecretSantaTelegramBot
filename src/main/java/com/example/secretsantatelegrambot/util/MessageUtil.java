package com.example.secretsantatelegrambot.util;

import com.example.secretsantatelegrambot.entity.Room;

import java.util.HashSet;
import java.util.Set;

public class MessageUtil {
    private static final String START_MESSAGE = """
           Добро пожаловать, @%s\\. Я бот Secret Santa и я помогу устроить Тебе и Твоим близким весёлый праздник\\!
           
           Если Ты первый раз слышишь о такой игре, то советую прочитать подробности, нажав на кнопку «Подробности»\\.
           """;

    public static final String JOIN_ROOM_MESSAGE = START_MESSAGE +
            """
            
            Тебя пригласили в комнату «%s»\\. Ожидай начала рандомизации или создай свою собственную комнату\\.
            """;

    private static final String YOURSELF_START_MESSAGE = START_MESSAGE +
            """
            
            Для начала ты можешь создать свою комнату или дождаться приглашения от других участников\\.
            """;

    public static final String HELP_MESSAGE = """
           Этот бот позволяет Тебе и Твоим друзьям устроить игру под названием «Тайный Санта»\\.
           
           Её цель заключается в том, чтобы организовать обмен подарками в новогодние праздники\\. При этом каждый участник, который дарит подарки \\(даритель\\), является анонимным\\. Единственное, что известно каждому дарителю \\- это тот участник, кому он будет дарить подарок \\(получатель\\)\\. После того, как дарителю станет известен получатель, он сам принимает решение, какой подарок ему вручить\\.
           
           Погрузитесь в незабываемое мероприятие вместе с Вашей семьёй или компанией друзей\\!
           """;

    public static final String CREATE_ROOM_WITH_NAME_ROOM_MESSAGE = """
            Введите название комнаты \\(название должно быть не короче 4 и не длинее 30 символов\\)\\.
            """;

    public static final String CREATE_ROOM_WITH_MIN_COUNT_USERS_MESSAGE = """
            Введите минимальное количество участников комнаты \\(Минимально возможное количество участников не менее 3 и не более 20\\)\\.
            """;

    public static final String CREATE_ROOM_WITH_MAX_COUNT_USERS_MESSAGE = """
            Введите максимальное количество участников комнаты \\(Максимально возможное количество участников не менее 3 и не более 20\\)\\.
            """;

    public static final String CREATE_ROOM_WITH_MIN_COST_GIFT_MESSAGE = """
            Введите минимальную стоимость подарка для комнаты \\(Более 0\\)\\.
            """;

    public static final String UPDATE_ROOM_MESSAGE = """
            Выберите, что вы хотите редактировать?
            """;

    public static final String UPDATE_ROOM_WITH_NAME_ROOM_MESSAGE = """
            Текущее название комнаты: «%s»\\.
            
            Введите новое название комнаты \\(название должно быть не длинее 30 символов\\)\\.
            """;

    public static final String UPDATE_ROOM_WITH_MIN_COUNT_USERS_MESSAGE = """
            Текущее минимальное количество участников: «%s»\\.
            
            Введите новое минимальное количество участников комнаты \\(Минимально возможное количество участников не менее 3\\)\\.
            """;

    public static final String UPDATE_ROOM_WITH_MAX_COUNT_USERS_MESSAGE = """
            Текущее максимальное количество участников: «%s»\\.
            
            Введите новое максимальное количество участников комнаты \\(Максимально возможное количество участников не более 20\\)\\.
            """;

    public static final String UPDATE_ROOM_WITH_MIN_COST_GIFT_MESSAGE = """
            Текущая минимальная стоимость подарка: «%s»\\.
            
            Введите минимальную стоимость подарка для комнаты \\(Более 0\\)\\.
            """;

    public static final String LIST_ROOMS_MESSAGE = """
            Список комнат представлен ниже\\.
            
            Выберите одну из комнат, чтобы просмотреть подробности\\.
            """;

    public static final String ROOM_DETAILS_MESSAGE = """
            *Комната:* «%s»
            *Ссылка для приглашения:* https://t\\.me/DodaSecretSantaBot?start\\=%s
            
            *Мин\\. кол\\-во участников:* %s
            *Макс\\. кол\\-во участников:* %s
            *Мин\\. стоимость подарков:* %s
            
            *Дата создания:* %s
            *Создатель:* @%s
            """;

    public static final String USERS_IN_ROOM_MESSAGE = """
            *Список участников:*
            
            %s
            """;

    public static final String JOIN_ROOM_START_RANDOMIZATION_CONFIRM_MESSAGE = """
            Количество участников комнаты «%s» достигло минимального количества\\.
            
            Хотите запустить рандомизацию участников или подождёте вступления остальных пользователей?
            """;

    public static final String START_RANDOMIZATION_CONFIRM_MESSAGE = """
            Вы уверены, что хотите начать процесс рандомизации в комнате «%s»?
            """;

    public static final String AFTER_RANDOMIZATION_MESSAGE = """
            Праздник начинается\\!\\!\\! В комнате «%s» произошла рандомизация участников\\. Ты готов узнать кому даришь подарок?
            
            И\\-и\\-и\\-и это же: ||@%s||\\.
            
            Подготовь для этого человека свой индивидуальный подарок\\. Удачи\\!
            """;

    public static final String FIND_RECEIVER_BY_GIVEN_MESSAGE = """
            Ты даришь подарок: ||@%s||\\.
            """;

    public static final String DELETE_USER_FROM_ROOM_WITH_USERNAME_MESSAGE = """
            Введите никнейм пользователя, которого хотите удалить \\(Например, @username или username\\)\\.
            """;

    public static final String DELETE_USER_FROM_ROOM_CONFIRM_MESSAGE = """
            Вы уверены, что хотите удалить участника @%s из комнаты «%s»?
            """;

    public static final String LEAVE_ROOM_USER_CONFIRM_MESSAGE = """
            Вы уверены, что хотите выйти из комнаты «%s»?
            """;

    public static final String LEAVE_ROOM_ADMIN_CONFIRM_MESSAGE = LEAVE_ROOM_USER_CONFIRM_MESSAGE +
            """
            Вы являетесь её *создателем*\\. Если вы выйдете из комнаты, то она автоматически *удалится*\\!
            """;

    public static final String EXCEPTION_MESSAGE = """
            В классе {} у пользователя {} во время исполнения сообщения/команды «{}» произошло исключение {}: {}
            """;

    private static final Set<Character> GENERAL_ESCAPE_CHARS = new HashSet<>() {{
        add('_'); add('*'); add('['); add(']'); add('('); add(')');
        add('~'); add('\''); add('>'); add('#'); add('+'); add('-');
        add('='); add('|'); add('{'); add('}'); add('.'); add('!');
    }};

    private static final Set<Character> PRE_CODE_ESCAPE_CHARS = new HashSet<>() {{
        add('\''); add('\\');
    }};

    private static final Set<Character> LINK_EMOJI_ESCAPE_CHARS = new HashSet<>() {{
        add(')'); add('\\');
    }};

    public static String getStartMessage(String username) {
        return YOURSELF_START_MESSAGE.formatted(escape(username));
    }

    public static String createRoomDetailsMessage(Room room) {
        return ROOM_DETAILS_MESSAGE.formatted(
                escape(room.getName()),
                escape(room.getId().toString()),
                room.getMinCountUsers(),
                room.getMaxCountUsers(),
                escape(room.getMinCostGift().toString()),
                escape(DateUtil.getStringFromDate(room.getCreatedDate())),
                escape(room.getAdminUser().getUsername())
        );
    }

    public static String escape(String input) {
        return escape(input, false, false);
    }

    public static String escape(String input, boolean inPreOrCode, boolean inLinkOrEmoji) {
        StringBuilder escaped = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (c == '\\') {
                escaped.append("\\\\");
            }
            else if (inPreOrCode && PRE_CODE_ESCAPE_CHARS.contains(c)) {
                escaped.append('\\').append(c);
            }
            else if (inLinkOrEmoji && LINK_EMOJI_ESCAPE_CHARS.contains(c)) {
                escaped.append('\\').append(c);
            }
            else if (GENERAL_ESCAPE_CHARS.contains(c)) {
                escaped.append('\\').append(c);
            }
            else {
                escaped.append(c);
            }
        }

        return escaped.toString();
    }
}
