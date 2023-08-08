package org.ssglobal.training.codes.service;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class PasswordGenerator {
	private final String SPECIAL_CHARACTERS = "!@#$%^&*()";
    private final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private final String NUMBERS = "0123456789";

    public String generatePassword() {
        StringBuilder password = new StringBuilder();

        char uppercaseLetter = getRandomUppercaseLetter();
        password.append(uppercaseLetter);

        char specialCharacter = getRandomSpecialCharacter();
        password.append(specialCharacter);

        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            boolean isNumber = random.nextBoolean();
            char character;
            if (isNumber) {
                character = getRandomNumber();
            } else {
                character = getRandomLowercaseLetter();
            }
            password.append(character);
        }

        return password.toString();
    }

    private char getRandomUppercaseLetter() {
        Random random = new Random();
        int index = random.nextInt(26);
        return (char) ('A' + index);
    }

    private char getRandomSpecialCharacter() {
        Random random = new Random();
        int index = random.nextInt(SPECIAL_CHARACTERS.length());
        return SPECIAL_CHARACTERS.charAt(index);
    }

    private char getRandomLowercaseLetter() {
        Random random = new Random();
        int index = random.nextInt(LOWERCASE_LETTERS.length());
        return LOWERCASE_LETTERS.charAt(index);
    }

    private char getRandomNumber() {
        Random random = new Random();
        int index = random.nextInt(NUMBERS.length());
        return NUMBERS.charAt(index);
    }

}
