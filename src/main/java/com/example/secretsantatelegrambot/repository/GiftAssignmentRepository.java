package com.example.secretsantatelegrambot.repository;

import com.example.secretsantatelegrambot.entity.GiftAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GiftAssignmentRepository extends JpaRepository<GiftAssignment, UUID> {
    boolean existsByRoomId(UUID roomId);

    Optional<GiftAssignment> findByGiverIdAndRoomId(UUID giverId, UUID roomId);

    void deleteAllByRoomId(UUID roomId);
}
