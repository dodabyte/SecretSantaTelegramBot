--liquibase formatted sql
--changeset Vadim_Miller:v1.0 localFilePath:01.000.00/users_room.sql

CREATE TABLE users_room
(
    room_id UUID NOT NULL,
    user_id UUID NOT NULL,
    FOREIGN KEY (room_id) REFERENCES rooms(room_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

--rollback drop table users_room;