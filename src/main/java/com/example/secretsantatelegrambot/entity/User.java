package com.example.secretsantatelegrambot.entity;

import com.example.secretsantatelegrambot.entity.enums.UserState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@NamedEntityGraph(name = "findByChatId", attributeNodes = {
        @NamedAttributeNode(value = "rooms")
    }
)
@NamedEntityGraph(name = "findByUsername", attributeNodes = {
        @NamedAttributeNode(value = "rooms"),
        @NamedAttributeNode(value = "adminRooms")
    }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    UUID id;

    @Column(name = "username", unique = true, nullable = false)
    @NotBlank
    @Size(max = 255)
    String username;

    @Column(name = "chat_id", unique = true, nullable = false)
    @NotNull
    Long chatId;

    @Column(name = "user_state", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    @NotBlank
    @Size(max = 50)
    UserState userState;

    @Column(name = "registration_date", nullable = false)
    LocalDateTime registrationDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_room",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "room_id"))
    @ToString.Exclude
    Set<Room> rooms;

    @ToString.Exclude
    @OneToMany(mappedBy = "adminUser", fetch = FetchType.LAZY)
    List<Room> adminRooms;
}