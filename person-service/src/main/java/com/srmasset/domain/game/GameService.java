package com.srmasset.domain.game;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
public class GameService {

    public String generateServerMovement() {
        String[] options = {MovementValueBO.PAPER.name(), MovementValueBO.ROCK.name(), MovementValueBO.SCISSORS.name()};
        int idx = (int) (Math.random() * options.length);
        return options[idx];
    }

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
}
