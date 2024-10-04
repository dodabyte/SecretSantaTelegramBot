--liquibase formatted sql
--changeset Vadim_Miller:v1.0 localFilePath:01.000.00/rooms.sql

CREATE TABLE rooms
(
    room_id UUID PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    max_count_users INT NOT NULL,
    min_count_users INT NOT NULL,
    min_cost_gift DECIMAL(16, 2) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    admin_user_id UUID not null,
    FOREIGN KEY (admin_user_id) REFERENCES users(user_id)
);

--rollback drop table rooms;