package org.siberteam.tumasov.test.services;

import java.util.Arrays;

public class StringUtil {
    public static String sortWordByAlphabet(String word) {
        final char[] charsOfWord = word.toCharArray();
        Arrays.sort(charsOfWord);
        return String.valueOf(charsOfWord);
    }
}
