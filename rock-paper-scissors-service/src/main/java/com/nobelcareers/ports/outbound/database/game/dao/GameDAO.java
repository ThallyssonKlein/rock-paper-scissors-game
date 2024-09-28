package com.nobelcareers.ports.outbound.database.game.dao;

import com.nobelcareers.ports.outbound.database.user.UserDAO;
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

    @ManyToOne
    @JoinColumn(name = "winner_id", nullable = true)
    private UserDAO winner;
}
