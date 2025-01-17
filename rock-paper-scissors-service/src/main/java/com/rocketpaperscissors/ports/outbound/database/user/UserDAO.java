package com.rocketpaperscissors.ports.outbound.database.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, name = "name")
    private String username;

    @Column(nullable = false, length = 50, name = "password")
    private String password;

    public UserDAO(Long id) {
        this.id = id;
    }
}
