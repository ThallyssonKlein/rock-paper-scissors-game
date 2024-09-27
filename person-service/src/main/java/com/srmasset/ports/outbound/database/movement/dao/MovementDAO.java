package com.srmasset.ports.outbound.database.movement.dao;

import com.srmasset.ports.outbound.database.game.dao.GameDAO;
import jakarta.persistence.*;
import lombok.*;

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
    private MovementValue value;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private GameDAO game;
}
