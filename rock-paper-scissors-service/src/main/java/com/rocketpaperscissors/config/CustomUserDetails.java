package com.rocketpaperscissors.config;

import com.rocketpaperscissors.ports.outbound.database.user.UserDAO;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class CustomUserDetails extends User {

    private final UserDAO userDAO;

    public CustomUserDetails(UserDAO userDAO) {
        super(userDAO.getUsername(), userDAO.getPassword(), List.of());
        this.userDAO = userDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }
}