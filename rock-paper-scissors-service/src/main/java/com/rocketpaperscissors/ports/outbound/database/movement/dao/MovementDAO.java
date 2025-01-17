package com.rocketpaperscissors.ports.outbound.database.movement.dao;

import com.rocketpaperscissors.ports.outbound.database.game.dao.GameDAO;
import com.rocketpaperscissors.ports.outbound.database.user.UserDAO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "movement")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MovementDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8, name = "value")
    private MovementValueDAO value;

    @Column(nullable = false, name = "salt", length = 32)
    private String salt;

    @Column(nullable = false, name = "hash", length = 64)
    private String hash;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private UserDAO player;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private GameDAO game;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
