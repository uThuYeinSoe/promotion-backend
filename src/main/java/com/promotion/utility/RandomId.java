package com.promotion.utility;

import java.security.SecureRandom;

public class RandomId {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomId() {
        StringBuilder sb = new StringBuilder(LENGTH);
        for(int i = 0; i < LENGTH; i++){
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }
}
