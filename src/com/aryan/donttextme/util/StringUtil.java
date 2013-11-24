package com.aryan.donttextme.util;

import android.content.Context;

import com.aryan.donttextme.R;

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
    public static String getHijriMonthName(Context context, int month){
        switch (month){
            case 1:
                return context.getString(R.string.month_farvardind);
            case 2:
                return context.getString(R.string.month_ordibehesht);
            case 3:
                return context.getString(R.string.month_khordad);
            case 4:
                return context.getString(R.string.month_tir);
            case 5:
                return context.getString(R.string.month_mordad);
            case 6:
                return context.getString(R.string.month_shahrivar);
            case 7:
                return context.getString(R.string.month_mehr);
            case 8:
                return context.getString(R.string.month_aban);
            case 9:
                return context.getString(R.string.month_azar);
            case 10:
                return context.getString(R.string.month_dey);
            case 11:
                return context.getString(R.string.month_bahman);
            case 12:
                return context.getString(R.string.month_esfand);
        }
        return "";
    }
}
