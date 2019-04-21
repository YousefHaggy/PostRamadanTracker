package com.yousefhaggy.postramadantracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TrackerDatabaseHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE `MainTable` ( `description` TEXT, `date` TEXT )";
    private static final String SQL_DELETE_ENTRIES ="DROP TABLE IF EXISTS MainTable";
   public static final String DATABASE_NAME="database.db";
   private  SQLiteDatabase database;
   public TrackerDatabaseHelper(Context context)
   {
       super(context,DATABASE_NAME,null,1);
   }
    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    db.execSQL(SQL_DELETE_ENTRIES);
    onCreate(db);
    }

}
