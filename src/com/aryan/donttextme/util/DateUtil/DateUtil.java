package com.aryan.donttextme.util.DateUtil;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static String reFormatDate(String dateStr) {
        if (dateStr.length() < 8) {
            return dateStr;
        }
        String year = dateStr.substring(0, 4);
        String month = dateStr.substring(4, 6);
        String day = dateStr.substring(6, 8);
        return year + "/" + month + "/" + day;
    }

    public static String reFormatTime(String timeStr) {

        if (timeStr.length() < 6) {
            int diff = 6 - timeStr.length();
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < diff; i++) {
                sb.append("0");
            }

            sb.append(timeStr);
            timeStr = sb.toString();
        }

        String hour = timeStr.substring(0, 2);
        String minute = timeStr.substring(2, 4);
        String second = timeStr.substring(4, 6);
        return hour + ":" + minute + ":" + second;
    }

    public static String toPersian(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        MiladiDate miladi = new MiladiDate();
        miladi.setYear(cal.get(Calendar.YEAR));
        miladi.setMonth(cal.get(Calendar.MONTH) + 1);
        miladi.setDayOfMonth(cal.get(Calendar.DAY_OF_MONTH));

        PersianDate persianDate = DateConverter.civilToPersian(miladi);
        int year = persianDate.getYear();
        int month = persianDate.getMonth();
        int day = persianDate.getDayOfMonth();
        int hour = getHour(cal);
        int minute = cal.get(Calendar.MINUTE);

        return year + "/" +
                (month < 10 ? "0" + month : month + "") +
                "/" + (day < 10 ? "0" + day : day + "") +
                "-" + (hour < 10 ? "0" + hour : hour + "") +
                ":" + (minute < 10 ? "0" + minute : minute + "");
    }

    public static PersianDate miladiToPersian(Date miladiDate) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(miladiDate);
        MiladiDate miladi = new MiladiDate();
        miladi.setYear(cal.get(Calendar.YEAR));
        miladi.setMonth(cal.get(Calendar.MONTH) + 1);
        miladi.setDayOfMonth(cal.get(Calendar.DAY_OF_MONTH));
        return DateConverter.civilToPersian(miladi);
    }

    public static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);

        return cal.getTime();
    }

    public static String toString(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int hour = getHour(cal);

        int minute = cal.get(Calendar.MINUTE);
        return year + "/" +
                (month < 10 ? "0" + month : month + "") +
                "/" + (day < 10 ? "0" + day : day + "") +
                " " + (hour < 10 ? "0" + hour : hour + "") +
                ":" + (minute < 10 ? "0" + minute : minute + "");
    }

    public static String getDate() {
        Calendar cal = Calendar.getInstance();
        Date d = cal.getTime();
        int hour = getHour(cal);

        int minute = cal.get(Calendar.MINUTE);
        int year = cal.get(Calendar.YEAR);
        String date;
        if (year > 2000) {
            PersianDate pd = miladiToPersian(d);
            date = toStringInShamsiFormat(pd);
        } else {
            date = toString(d);
        }

        return date +
                "-" + (hour < 10 ? "0" + hour : hour + "") +
                ":" + (minute < 10 ? "0" + minute : minute + "");
    }

    public static long getLongDate() {
        Calendar cal = Calendar.getInstance();
        Date d = cal.getTime();
        return d.getTime();
    }

    public static String addSlashToShamsiDate(String date) {
        StringBuffer sb = new StringBuffer();
        if (date == null || date.length() < 8) {
            return date;
        }
        sb.append(date.substring(0, 4));
        sb.append("/");
        sb.append(date.substring(4, 6));
        sb.append("/");
        sb.append(date.substring(6, 8));

        return sb.toString();
    }

    public static String toStringInShamsiFormat(PersianDate persianDate) {
        int day = persianDate.getDayOfMonth();
        int month = persianDate.getMonth();
        int year = persianDate.getYear();
        return year + "/" +
                (month < 10 ? "0" + month : "" + month) + "/" +
                (day < 10 ? "0" + day : "" + day);
    }

    /**
     * identfies if the currentDate parameter is older than today
     *
     * @param date the Date
     * @param day the number of days which has to been passed
     * @return true if the Date parameter is old, false if it is not
     */
    public static boolean isOlderThanXDays(Date date, int day) {

        Calendar now = Calendar.getInstance();
        Calendar past = Calendar.getInstance();
        past.setTime(date);

        if (past.get(Calendar.YEAR) < now.get(Calendar.YEAR)) {
            return true;

        } else if (past.get(Calendar.MONTH) < now.get(Calendar.MONTH)) {
            return true;

        } else if (past.get(Calendar.DAY_OF_MONTH) < now.get(Calendar.DAY_OF_MONTH)) {

            if ((now.get(Calendar.DAY_OF_MONTH) - past.get(Calendar.DAY_OF_MONTH)) > day)
                return true;

            else if ((now.get(Calendar.DAY_OF_MONTH) - past.get(Calendar.DAY_OF_MONTH)) == day) {
                int pastHour;
                int nowHour;
                pastHour = getHour(past);
                nowHour = getHour(now);

                return pastHour < nowHour
                        || (pastHour == nowHour && (past.get(Calendar.MINUTE) < now.get(Calendar.MINUTE)));


            } else {
                return false;
            }

        } else {
            // refering to same day
            return false;
        }

    }

    public static int getHour(Calendar cal) {

        int hour = cal.get(Calendar.HOUR);
        if (cal.get(Calendar.AM_PM) == Calendar.PM) {
            if (hour != 12) {
                hour += 12;
            }
        } else {
            if (hour == 12) {
                hour = 0;
            }
        }

        return hour;
    }
}
