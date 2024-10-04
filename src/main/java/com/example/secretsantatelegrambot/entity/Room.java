package com.example.secretsantatelegrambot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@NamedEntityGraph(name = "findById", attributeNodes = {
        @NamedAttributeNode(value = "users"),
        @NamedAttributeNode(value = "adminUser")
    }
)
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "room_id")
    UUID id;

    @Column(name = "name", length = 30, nullable = false)
    @NotBlank
    @Size(max = 30)
    String name;

    @Column(name = "max_count_users", nullable = false)
    @NotNull
    int maxCountUsers;

    @Column(name = "min_count_users", nullable = false)
    @NotNull
    int minCountUsers;

    @Column(name = "min_cost_gift", nullable = false, precision = 16, scale = 2)
    @NotNull
    BigDecimal minCostGift;

    @Column(name = "created_date", nullable = false)
    @NotNull
    LocalDateTime createdDate;

    @ManyToMany(mappedBy = "rooms")
    @ToString.Exclude
    Set<User> users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_user_id", nullable = false)
    @ToString.Exclude
    User adminUser;
}
