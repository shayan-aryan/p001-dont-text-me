package com.aryan.donttextme.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.SmsMessage;

import com.aryan.donttextme.model.SMS;

import java.util.ArrayList;

/**
 * Created by Shayan on 11/18/13.
 */
public class DataBaseManager extends SQLiteOpenHelper {

    private SmsMessage SMS;

    private static final String DATABASE_NAME = "dont_text";
    private static final int DATABASE_VERSION = 1;
    private static final String lock = "lock";
    private static final String TABLE_INBOX = "inbox";
    private static final String TABLE_BLACKLIST = "black_list";
    private static final String TABLE_WHITE_LIST = "white_list";
    private static final String TABLE_SETTINGS = "settings";


    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_SENDER = "sender";
    private static final String COLUMN_BODY = "body";
    private static final String COLUMN_UNREAD = "unread";
    private static final String COLUMN_FILTER_KEY = "filter_key";
    private static final String COLUMN_SPECIFIC_NUMBER_FILTER = "specific_number_filter";
    private static final String COLUMN_STARTING_NUMBER_FILTER = "starting_number_filter";
    private static final String COLUMN_KEYWORD_FILTER = "keyword_filter";
    private static final String COLUMN_NUMBER_RANGE_FILTER = "number_range_filter";
    private static final String COLUMN_NAME = "name";

    private static final int COLUMN_INDEX_FILTER_KEY = 0;
    private static final int COLUMN_INDEX_COLUMN_NAME = 1;
    private static final int COLUMN_INDEX_SPECIFIC_NUMBER = 2;
    private static final int COLUMN_INDEX_STARTING_NUMBER = 3;
    private static final int COLUMN_INDEX_NUMBER_RANGE = 4;
    private static final int COLUMN_INDEX_KEYWORD = 5;

    private static final String PLUS = "+";
    private static final String SEPARATOR = "-";


    private static final String CREATE_STATEMENT_INBOX = String.format(
            "CREATE TABLE  %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s BOOLEAN, %s INTEGER)",
            TABLE_INBOX, COLUMN_TIME, COLUMN_SENDER, COLUMN_BODY, COLUMN_UNREAD, COLUMN_FILTER_KEY);
    private static final String CREATE_STATEMENT_BLACKLIST = String.format(
            "CREATE TABLE  %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TABLE_BLACKLIST, COLUMN_FILTER_KEY, COLUMN_NAME, COLUMN_SPECIFIC_NUMBER_FILTER, COLUMN_STARTING_NUMBER_FILTER, COLUMN_NUMBER_RANGE_FILTER, COLUMN_KEYWORD_FILTER);
    private static final String CREATE_STATEMENT_WHITE_LIST = String.format(
            "CREATE TABLE  %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TABLE_WHITE_LIST, COLUMN_FILTER_KEY, COLUMN_NAME, COLUMN_SPECIFIC_NUMBER_FILTER, COLUMN_STARTING_NUMBER_FILTER, COLUMN_NUMBER_RANGE_FILTER, COLUMN_KEYWORD_FILTER);

    public DataBaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATEMENT_INBOX);
        db.execSQL(CREATE_STATEMENT_BLACKLIST);
        db.execSQL(CREATE_STATEMENT_WHITE_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }



    public ArrayList<SMS> getAllSms() {
        ArrayList<SMS> list = new ArrayList<SMS>();
        SQLiteDatabase db = getReadableDatabase();
        synchronized (lock) {
            Cursor cursor = db.rawQuery(String.format("SELECT %s, %s, %s FROM %s", COLUMN_TIME, COLUMN_SENDER, COLUMN_BODY, TABLE_INBOX), null);
            long time;
            String sender;
            String body;

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                time = cursor.getLong(0);
                sender = cursor.getString(1);
                body = cursor.getString(2);
                list.add(new SMS(time, sender, body, true, 1));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list;
    }

    public boolean isInBlackListAndNotInWhiteList(SmsMessage smsMessage) {
        SMS = smsMessage;
        SQLiteDatabase db = getReadableDatabase();
        synchronized (lock) {
            Cursor cursor = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s='%s' AND %s NOT IN(SELECT %s FROM %s)"
                    , COLUMN_FILTER_KEY, TABLE_BLACKLIST, COLUMN_SPECIFIC_NUMBER_FILTER, smsMessage.getOriginatingAddress()
                    , COLUMN_SPECIFIC_NUMBER_FILTER, COLUMN_SPECIFIC_NUMBER_FILTER, TABLE_WHITE_LIST), null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {// sender number is in specific numbers
                AddToInbox(smsMessage, cursor.getInt(0));
                db.close();
                return true;
            }
            cursor = db.rawQuery(String.format("SELECT %s,%s FROM %s WHERE %s NOT IN (SELECT %s FROM %s)"
                    , COLUMN_FILTER_KEY, COLUMN_KEYWORD_FILTER, TABLE_BLACKLIST
                    , COLUMN_KEYWORD_FILTER, COLUMN_KEYWORD_FILTER, TABLE_WHITE_LIST), null);
            if (hasTheKeywords(cursor, smsMessage.getMessageBody())) {
                db.close();
                return true;
            }
            cursor = db.rawQuery(String.format("SELECT %s,%s FROM %s WHERE %s NOT IN (SELECT %s FROM %s)"
                    , COLUMN_FILTER_KEY, COLUMN_STARTING_NUMBER_FILTER, TABLE_BLACKLIST
                    , COLUMN_STARTING_NUMBER_FILTER, COLUMN_STARTING_NUMBER_FILTER, TABLE_WHITE_LIST), null);
            if (senderNumberIsStartingWith(cursor, smsMessage.getOriginatingAddress())) {
                db.close();
                return true;
            }
            cursor = db.rawQuery(String.format("SELECT %s,%s FROM %s WHERE %s NOT IN (SELECT %s FROM %s)"
                    , COLUMN_FILTER_KEY, COLUMN_NUMBER_RANGE_FILTER, TABLE_BLACKLIST
                    , COLUMN_NUMBER_RANGE_FILTER, COLUMN_NUMBER_RANGE_FILTER, TABLE_WHITE_LIST), null);
            if (isInNumberRanges(cursor, smsMessage.getOriginatingAddress())) {
                db.close();
                return true;
            }
        }

        db.close();
        return false;
    }

    private boolean isInNumberRanges(Cursor cursor, String senderNumber) {
        cursor.moveToFirst();
        long number = Long.getLong(senderNumber.replace(PLUS, ""));
        long[] range = new long[2];
        String[] temp;
        while (!cursor.isAfterLast()) {
            temp = cursor.getString(1).split(SEPARATOR);
            range[0] = Long.getLong(temp[0]);
            range[1] = Long.getLong(temp[1]);
            if (number > range[0] && number < range[1]) {
                AddToInbox(SMS, cursor.getInt(COLUMN_INDEX_FILTER_KEY));
                return true;
            }
            cursor.moveToNext();
        }
        return false;
    }

    private boolean senderIsInSpecificNumbers(Cursor cursor) {
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            AddToInbox(SMS, cursor.getInt(COLUMN_INDEX_FILTER_KEY));
            return true;
        } else
            return false;
    }

    private boolean hasTheKeywords(Cursor cursor, String smsBody) {
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (smsBody.toLowerCase().contains(cursor.getString(1).toLowerCase())) {
                AddToInbox(SMS, cursor.getInt(COLUMN_INDEX_FILTER_KEY));
                return true;
            }
            cursor.moveToNext();
        }
        return false;
    }

    private boolean senderNumberIsStartingWith(Cursor cursor, String senderNumber) {
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (senderNumber.startsWith(cursor.getString(1))) {
                AddToInbox(SMS, cursor.getInt(COLUMN_INDEX_FILTER_KEY));
                return true;
            }
            cursor.moveToNext();
        }
        return false;
    }

    //region Add & Remove
    public void AddToInbox(SmsMessage smsMessage, int filterKey) {
        long time = smsMessage.getTimestampMillis();
        String sender = smsMessage.getOriginatingAddress();
        String body = smsMessage.getMessageBody();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        synchronized (lock) {
            cv.put(COLUMN_TIME, time);
            cv.put(COLUMN_SENDER, sender);
            cv.put(COLUMN_BODY, body);
            cv.put(COLUMN_UNREAD, true);
            cv.put(COLUMN_FILTER_KEY, filterKey);
            db.insert(TABLE_INBOX, null, cv);
        }
        db.close();
    }

    public void AddToSpecificNumbersBlackList(String number) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        synchronized (lock) {
            cv.put(COLUMN_SPECIFIC_NUMBER_FILTER, number);
            db.insert(TABLE_BLACKLIST, null, cv);
        }
        db.close();
    }

    public void AddToSpecificNumbersWhiteList(String number) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        synchronized (lock) {
            cv.put(COLUMN_SPECIFIC_NUMBER_FILTER, number);
            db.insert(TABLE_WHITE_LIST, null, cv);
        }
        db.close();
    }

    public void AddToKeywordsBlackList(String keyword) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        synchronized (lock) {
            cv.put(COLUMN_KEYWORD_FILTER, keyword);
            db.insert(TABLE_BLACKLIST, null, cv);
        }
        db.close();
    }

    public void AddToKeywordsWhiteList(String keyword) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        synchronized (lock) {
            cv.put(COLUMN_KEYWORD_FILTER, keyword);
            db.insert(TABLE_WHITE_LIST, null, cv);
        }
        db.close();
    }

    public void AddToStartingNumbersBlackList(String number) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        synchronized (lock) {
            cv.put(COLUMN_STARTING_NUMBER_FILTER, number);
            db.insert(TABLE_BLACKLIST, null, cv);
        }
        db.close();
    }

    public void AddToStartingNumbersWhiteList(String number) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        synchronized (lock) {
            cv.put(COLUMN_STARTING_NUMBER_FILTER, number);
            db.insert(TABLE_WHITE_LIST, null, cv);
        }
        db.close();
    }

    public void AddToNumberRangesBlackList(long[] range) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        String rangeText = String.valueOf(range[0]) + SEPARATOR + String.valueOf(range[1]);
        synchronized (lock) {
            cv.put(COLUMN_NUMBER_RANGE_FILTER, rangeText);
            db.insert(TABLE_BLACKLIST, null, cv);
        }
        db.close();
    }

    public void AddToNumberRangesWhiteList(long[] range) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        String rangeText = String.valueOf(range[0]) + SEPARATOR + String.valueOf(range[1]);
        synchronized (lock) {
            cv.put(COLUMN_NUMBER_RANGE_FILTER, rangeText);
            db.insert(TABLE_WHITE_LIST, null, cv);
        }
        db.close();
    }
    //end of region


    public void eraseInbox() {
        SQLiteDatabase db = getWritableDatabase();
        synchronized (lock) {
            db.delete(TABLE_INBOX, null, null);
        }
        db.close();
    }

    public void eraseBlacklist() {
        SQLiteDatabase db = getWritableDatabase();
        synchronized (lock) {
            db.delete(TABLE_BLACKLIST, null, null);
        }
        db.close();
    }
}
