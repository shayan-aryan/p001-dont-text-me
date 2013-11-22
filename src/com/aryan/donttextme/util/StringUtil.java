package com.aryan.donttextme.util;

/**
 * Created by Shayan on 11/22/13.
 */
public class StringUtil {

    public static boolean IsNumber(String address) {
        char ch = address.charAt(0);
        switch (ch) {
            case '+':
                return true;
            case '0':
                return true;
            case '3':
                return true;
            case '5':
                return true;
            case '4':
                return true;
            case '7':
                return true;
            case '1':
                return true;
            case '8':
                return true;
            case '2':
                return true;
            case '9':
                return true;
        }
        return false;
    }
}
