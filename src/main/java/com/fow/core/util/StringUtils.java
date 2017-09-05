package com.fow.core.util;

/**
 * Created by greg.chen on 14-10-10.
 */
public final class StringUtils {

    public static boolean hasText(String text){
        if(text == null)
            return false;

        for(int i=0; i < text.length(); i++){
            if(!Character.isWhitespace(text.charAt(i)))
                return true;
        }

        return false;
    }

    public static boolean equals(String text1, String text2) {
        if (text1 == null)
            return text2 == null;

        return text1.equals(text2);
    }

    public static String trim(String text) {
        if (text == null)
            return null;
        return text.trim();
    }
}
