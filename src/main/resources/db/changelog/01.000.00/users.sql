--liquibase formatted sql
--changeset Vadim_Miller:v1.0 localFilePath:01.000.00/users.sql

CREATE TABLE users
(
    user_id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    chat_id BIGINT NOT NULL,
    user_state VARCHAR(50) NOT NULL CHECK ( user_state IN ('START',
                                                           'AWAITING_CREATE_NAME_ROOM',
                                                           'AWAITING_CREATE_MIN_COUNT_USERS_ROOM',
                                                           'AWAITING_CREATE_MAX_COUNT_USERS_ROOM',
                                                           'AWAITING_CREATE_MIN_COST_GIFT_ROOM',
                                                           'AWAITING_UPDATE_NAME_ROOM',
                                                           'AWAITING_UPDATE_MIN_COUNT_USERS_ROOM',
                                                           'AWAITING_UPDATE_MAX_COUNT_USERS_ROOM',
                                                           'AWAITING_UPDATE_MIN_COST_GIFT_ROOM',
                                                           'AWAITING_START_RANDOMIZATION',
                                                           'AWAITING_USERNAME_DELETE',
                                                           'AWAITING_DELETE_CONFIRM',
                                                           'AWAITING_CONFIRM_LEAVE_ROOM') ),
    registration_date TIMESTAMP NOT NULL
);

--rollback drop table users;