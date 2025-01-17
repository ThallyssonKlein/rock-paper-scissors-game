package com.rocketpaperscissors.ports.outbound.database.result;

import com.rocketpaperscissors.ports.outbound.database.game.dao.GameDAO;
import com.rocketpaperscissors.ports.outbound.database.user.UserDAO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "result")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ResultDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 4, name = "result")
    private ResultValueDAO result;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private UserDAO player;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private GameDAO game;
}
