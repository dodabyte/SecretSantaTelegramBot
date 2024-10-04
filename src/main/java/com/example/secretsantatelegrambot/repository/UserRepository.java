package com.example.secretsantatelegrambot.repository;

import com.example.secretsantatelegrambot.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @EntityGraph(value = "findByChatId", type = EntityGraph.EntityGraphType.FETCH)
    Optional<User> findByChatId(Long chatId);

    @EntityGraph(value = "findByUsername", type = EntityGraph.EntityGraphType.FETCH)
    Optional<User> findByUsername(String username);
}
