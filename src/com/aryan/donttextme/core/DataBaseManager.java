package com.aryan.donttextme.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shayan on 11/18/13.
 */
public class DataBaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DontText";
    private static final int DATABASE_VERSION = 1;

    public DataBaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
