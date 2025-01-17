package com.rocketpaperscissors.ports.outbound.database.game.dao;

import com.rocketpaperscissors.ports.outbound.database.user.UserDAO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "game")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class GameDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, name = "next_move")
    private NextMoveDAO nextMoveDAO;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, name = "status")
    private StatusDAO statusDAO;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserDAO owner;

    public GameDAO(Long id) {
        this.id = id;
    }
}
