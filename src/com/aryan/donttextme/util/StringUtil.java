package com.aryan.donttextme.util;

/**
 * Created by Shayan on 11/22/13.
 */
public class StringUtil {

    public static boolean IsNumber(String address) {
        if (address.startsWith("+"))
            return true;
        else if (address.startsWith("0"))
            return true;
        else if (address.startsWith("3"))
            return true;
        else if (address.startsWith("5"))
            return true;
        else if (address.startsWith("4"))
            return true;
        else if (address.startsWith("7"))
            return true;
        else if (address.startsWith("8"))
            return true;
        else if (address.startsWith("6"))
            return true;
        else if (address.startsWith("9"))
            return true;
        else if (address.startsWith("2"))
            return true;
        else if (address.startsWith("1"))
            return true;
        else
            return false;
    }
}
