package com.thitsaworks.operation_portal.component.misc.util;

import java.util.Random;

public class RandomNumber {

    public static String generate(int length) {

        String numbers = "1234567890";
        Random random = new Random();

        char[] otp = new char[length];

        for (int i = 0; i < length; i++) {

            otp[i] = numbers.charAt(random.nextInt(numbers.length()));

        }

        return String.valueOf(otp);

    }

}