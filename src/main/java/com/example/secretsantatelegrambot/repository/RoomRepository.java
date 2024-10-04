package com.example.secretsantatelegrambot.repository;

import com.example.secretsantatelegrambot.entity.Room;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {
    @Override
    @EntityGraph(value = "findById", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Room> findById(UUID roomId);
}
