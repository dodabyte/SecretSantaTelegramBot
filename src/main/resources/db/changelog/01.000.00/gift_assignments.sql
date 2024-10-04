--liquibase formatted sql
--changeset Vadim_Miller:v1.0 localFilePath:01.000.00/gift_assignments.sql

CREATE TABLE gift_assignments
(
    gift_assignment_id UUID PRIMARY KEY,
    giver_id UUID not null,
    receiver_id UUID not null,
    room_id UUID not null,
    FOREIGN KEY (giver_id) REFERENCES users(user_id),
    FOREIGN KEY (receiver_id) REFERENCES users(user_id),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);

--rollback drop table gift_assignments;