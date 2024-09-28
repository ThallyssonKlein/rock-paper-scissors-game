package com.nobelcareers.adapters.outbound.database;

import com.nobelcareers.ports.inbound.http.api.v1.exception.ForbiddenException;
import com.nobelcareers.ports.outbound.database.user.OutboundUserRepositoryPort;
import com.nobelcareers.ports.outbound.database.user.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class OutboundUserAdapterTest {

    @Mock
    private OutboundUserRepositoryPort outboundUserRepositoryPort;

    @InjectMocks
    private OutboundUserAdapter outboundUserAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("findByUsername returns UserDAO when user is found")
    void findByUsernameReturnsUserDAOWhenUserIsFound() throws ForbiddenException {
        String username = "testUser";
        UserDAO userDAO = new UserDAO();
        when(outboundUserRepositoryPort.findByUsername(username)).thenReturn(Optional.of(userDAO));

        UserDAO result = outboundUserAdapter.findByUsername(username).get();

        assertEquals(userDAO, result);
    }

    @Test
    @DisplayName("findByUsername throws ForbiddenException when user is not found")
    void findByUsernameThrowsForbiddenExceptionWhenUserIsNotFound() {
        String username = "nonExistentUser";
        when(outboundUserRepositoryPort.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(ForbiddenException.class, () -> outboundUserAdapter.findByUsername(username));
    }
}