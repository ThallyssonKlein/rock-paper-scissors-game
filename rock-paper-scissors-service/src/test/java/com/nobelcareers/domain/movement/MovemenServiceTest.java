package com.nobelcareers.domain.movement;

import com.nobelcareers.domain.game.bo.MovementBO;
import com.nobelcareers.domain.game.bo.MovementValueBO;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MovementServiceTest {

    private final MovementService movementService = new MovementService();

    @Test
    void generateSalt_shouldReturnNonEmptyString() {
        String salt = movementService.generateSalt();
        assertNotNull(salt);
        assertFalse(salt.isEmpty());
    }

    @Test
    void generateHash_shouldReturnConsistentHash() {
        String move = "ROCK";
        String salt = "1234567890abcdef";
        String hash1 = movementService.generateHash(move, salt);
        String hash2 = movementService.generateHash(move, salt);
        assertEquals(hash1, hash2);
    }

    @Test
    void generateHash_shouldReturnDifferentHashesForDifferentSalts() {
        String move = "ROCK";
        String salt1 = "1234567890abcdef";
        String salt2 = "abcdef1234567890";
        String hash1 = movementService.generateHash(move, salt1);
        String hash2 = movementService.generateHash(move, salt2);
        assertNotEquals(hash1, hash2);
    }

    @Test
    void generateServerMovement_shouldReturnScissorsWhenPaperIsMostFrequent() {
        List<MovementBO> movements = Arrays.asList(
                new MovementBO(MovementValueBO.PAPER),
                new MovementBO(MovementValueBO.PAPER),
                new MovementBO(MovementValueBO.ROCK)
        );
        String result = movementService.generateServerMovement(movements);
        assertEquals(MovementValueBO.SCISSORS.name(), result);
    }

    @Test
    void generateServerMovement_shouldReturnPaperWhenRockIsMostFrequent() {
        List<MovementBO> movements = Arrays.asList(
                new MovementBO(MovementValueBO.ROCK),
                new MovementBO(MovementValueBO.ROCK),
                new MovementBO(MovementValueBO.SCISSORS)
        );
        String result = movementService.generateServerMovement(movements);
        assertEquals(MovementValueBO.PAPER.name(), result);
    }

    @Test
    void generateServerMovement_shouldReturnRockWhenScissorsIsMostFrequent() {
        List<MovementBO> movements = Arrays.asList(
                new MovementBO(MovementValueBO.SCISSORS),
                new MovementBO(MovementValueBO.SCISSORS),
                new MovementBO(MovementValueBO.PAPER)
        );
        String result = movementService.generateServerMovement(movements);
        assertEquals(MovementValueBO.ROCK.name(), result);
    }

    @Test
    void generateServerMovement_shouldReturnRandomOptionWhenMovementsIsEmpty() {
        List<MovementBO> movements = Collections.emptyList();
        String result = movementService.generateServerMovement(movements);
        assertTrue(Arrays.asList("PAPER", "ROCK", "SCISSORS").contains(result));
    }
}