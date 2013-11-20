package com.aryan.donttextme.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.SmsMessage;

import com.aryan.donttextme.model.SMS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private static final String TABLE_SETTINGS = "settings";


    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_SENDER = "sender";
    private static final String COLUMN_BODY = "body";
    private static final String COLUMN_UNREAD = "unread";
    private static final String COLUMN_FILTER_KEY = "filter_key";
    private static final String COLUMN_SPECIFIC_NUMBER_FILTER = "specific_number_filter";
    private static final String COLUMN_STARTING_NUMBER_FILTER = "starting_number_filter";
    private static final String COLUMN_CONTAINING_TEXT_FILTER = "containing_text_filter";
    private static final String COLUMN_FROM_NUMBER_FILTER = "from_number_filter";
    private static final String COLUMN_TO_NUMBER_FILTER = "to_number_filter";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_ENABLED = "name";


    private static final String CREATE_STATEMENT_SMS = String.format(
            "CREATE TABLE  %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s BOOLEAN, %s INTEGER)",
            TABLE_INBOX, COLUMN_TIME, COLUMN_SENDER, COLUMN_BODY, COLUMN_UNREAD, COLUMN_FILTER_KEY);
    private static final String CREATE_STATEMENT_BLACKLIST = String.format(
            "CREATE TABLE  %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s BOOLEAN)",
            TABLE_BLACKLIST, COLUMN_FILTER_KEY, COLUMN_NAME, COLUMN_SPECIFIC_NUMBER_FILTER, COLUMN_STARTING_NUMBER_FILTER, COLUMN_FROM_NUMBER_FILTER, COLUMN_TO_NUMBER_FILTER, COLUMN_CONTAINING_TEXT_FILTER, COLUMN_ENABLED);

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

    public ArrayList<SMS> getAllSms(){
        ArrayList<SMS> list = new ArrayList<SMS>();
        SQLiteDatabase db = getReadableDatabase();
        synchronized (lock){
            Cursor cursor = db.rawQuery(String.format("SELECT %s, %s, %s FROM %s", COLUMN_TIME, COLUMN_SENDER, COLUMN_BODY, TABLE_INBOX),null);
            long time;
            String sender;
            String body;

            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
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

    public void eraseInbox(){
        SQLiteDatabase db = getWritableDatabase();
        synchronized (lock){
            db.delete(TABLE_INBOX, null, null );
        }
        db.close();
    }

    public boolean isInBlackList(SmsMessage smsMessage){
        SMS = smsMessage;
        SQLiteDatabase db = getReadableDatabase();
        synchronized (lock){
            Cursor cursor = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s='%s'", COLUMN_FILTER_KEY, TABLE_BLACKLIST, COLUMN_SPECIFIC_NUMBER_FILTER, smsMessage.getOriginatingAddress()),null);
            cursor.moveToFirst();
           if(!cursor.isAfterLast()){
                AddToInbox(smsMessage, cursor.getInt(0));
                db.close();
                return true;
            }
            cursor = db.rawQuery(String.format("SELECT %s,%s,%s,%s FROM %s", COLUMN_STARTING_NUMBER_FILTER, COLUMN_FROM_NUMBER_FILTER, COLUMN_TO_NUMBER_FILTER, COLUMN_CONTAINING_TEXT_FILTER, TABLE_BLACKLIST),null);
            isStartingWithNumber(cursor, smsMessage.getOriginatingAddress());
        }
        db.close();
    }

    private boolean isStartingWithNumber(Cursor cursor, String number){

    }
}
