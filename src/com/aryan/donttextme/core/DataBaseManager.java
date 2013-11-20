package com.aryan.donttextme.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shayan on 11/18/13.
 */
public class DataBaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dont_text";
    private static final int DATABASE_VERSION = 1;
    private static final String lock = "lock";
    private static final String TABLE_INBOX = "inbox";
    private static final String TABLE_BLACKLIST_SPECIFIC_NUMBER = "specific_number_blacklist";
    private static final String TABLE_BLACKLIST_STARTING_NUMBER = "starting_number_blacklist";
    private static final String TABLE_BLACKLIST_CONTAINING_STRING = "containing_string_blacklist";
    private static final String TABLE_BLACKLIST_FROM_TO_NUMBER = "from_to_number_blacklist";
    private static final String TABLE_SETTINGS = "settings";


    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_BODY = "body";
    private static final String COLUMN_UNREAD = "unread";

    private static final String CREATE_STATEMENT_SMS = String.format("CREATE TABLE  %s (%s TEXT PRIMARY KEY, %s TEXT, %s TEXT, %s BOOLEAN)",
            TABLE_INBOX, COLUMN_TIME, COLUMN_ADDRESS, COLUMN_BODY, COLUMN_UNREAD);

    public DataBaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATEMENT_SMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public void AddToInbox(String time, String address, String body) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        synchronized (lock) {
            cv.put(COLUMN_TIME, time);
            cv.put(COLUMN_ADDRESS, address);
            cv.put(COLUMN_BODY, body);
            cv.put(COLUMN_UNREAD, true);
            db.insert(TABLE_INBOX, null, cv);
        }
        db.close();
    }
}
