package com.zubiisoft.instazub.utils;

import androidx.annotation.NonNull;

import java.util.Random;

public class Util {

    @NonNull
    public static String getRandomUid() {
        int length = 28;

        String symbol = "-/.^&*_!@%=+>)";
        String capLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String smallLetter = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String finalString =  capLetter + smallLetter + numbers;

        Random random = new Random();

        char[] uid = new char[length];

        for (int i = 0; i < length; i++) {
            uid[i] = finalString.charAt(random.nextInt(finalString.length()));
        }

        return String.valueOf(uid);
    }

}
