package com.nobelcareers.domain.movement;

import com.nobelcareers.domain.game.bo.MovementBO;
import com.nobelcareers.domain.game.bo.MovementValueBO;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

@Service
public class MovementService {

    private final String[] OPTIONS = {MovementValueBO.PAPER.name(), MovementValueBO.ROCK.name(), MovementValueBO.SCISSORS.name()};

    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return bytesToHex(saltBytes);
    }

    public String generateHash(String move, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String text = move + ":" + salt;
            byte[] hashBytes = md.digest(text.getBytes());
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = String.format("%02x", b);
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String generateServerMovement(List<MovementBO> movements) {
        if (!movements.isEmpty()) {
            long paperCount = movements.stream().filter(m -> m.getValue().equals(MovementValueBO.PAPER)).count();
            long rockCount = movements.stream().filter(m -> m.getValue().equals(MovementValueBO.ROCK)).count();
            long scissorsCount = movements.stream().filter(m -> m.getValue().equals(MovementValueBO.SCISSORS)).count();

            if (paperCount > rockCount && paperCount > scissorsCount) {
                return MovementValueBO.SCISSORS.name();
            } else if (rockCount > paperCount && rockCount > scissorsCount) {
                return MovementValueBO.PAPER.name();
            } else {
                return MovementValueBO.ROCK.name();
            }
        } else {
            return OPTIONS[(int) (Math.random() * OPTIONS.length)];
        }
    }
}
