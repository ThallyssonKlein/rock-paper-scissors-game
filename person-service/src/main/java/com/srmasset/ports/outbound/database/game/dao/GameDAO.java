package com.srmasset.ports.outbound.database.game.dao;

import com.srmasset.ports.outbound.database.movement.dao.MovementDAO;
import com.srmasset.ports.outbound.database.user.UserDAO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    private NextMove nextMove;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserDAO owner;
}
