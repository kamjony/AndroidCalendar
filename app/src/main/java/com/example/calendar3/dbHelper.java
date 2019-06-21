package com.example.calendar3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by KamrulHasan on 4/7/2017.
 */

public class dbHelper extends SQLiteOpenHelper {
    //declaring variables
    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "appointment.db"; //database name
    private static final int DATABASE_VERSION = 1; //database version
    public static final String TABLE_NAME = "appointments";
    public static final String COL1 = "ID";
    public static final String COL2 = "date";
    public static final String COL3 = "time";
    public static final String COL4 = "title";
    public static final String COL5 = "details";

    //creates the database table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL2 + " NUMERIC, " +
                    COL3 + " NUMERIC, " +
                    COL4 + " TEXT, " +
                    COL5 + " TEXT " +
                    ")";


    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //add data sql
    public boolean addData(String date, String time, String title, String details) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, date);
        contentValues.put(COL3, time);
        contentValues.put(COL4, title);
        contentValues.put(COL5, details);

        Log.d(TAG, "addData: Adding " + date + ", " + time + ", " + title + ", " + details + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns all the data from database
     *
     * @return
     */
    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Returns all the data from database
     * @param title
     * @return
     */
    public Cursor getData2(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL4 + " = '" + title + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Returns all the data from database
     * @param date
     * @return
     */
    public Cursor getDataDate(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL2 + " = '" + date + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Returns COL1=id from database
     * @param title
     * @return
     */
    public Cursor getItemID(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL4 + " = '" + title + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Returns COL1=id from database
     * @param title
     * @param time
     * @return
     */
    public Cursor getItemID2(String time, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL4 + " = '" + title + "'" + " AND " + COL3 + " = '" + time + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    /**
     * Delete from database
     *
     * @param id
     * @param title
     */
    public void deleteData(int id, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + COL4 + " = '" + title + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + title + " from database.");
        db.execSQL(query);
    }

    /**
     * Delete all columns from database
     *
     * @param date
     */
    public void deleteAll(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL2 + " = '" + date + "'";
        db.execSQL(query);
    }

    /**
     * Updates time,title,details columns from database
     *
     * @param id,time,title,details
     */
    public boolean updateData(int id, String time, String title, String details) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(COL3, time);
        args.put(COL4, title);
        args.put(COL5, details);
        return db.update(TABLE_NAME, args, COL1 + "=" + id, null) > 0;

    }

    /**
     * Updates date column from database
     *
     * @param id,time,title,details
     */
    public boolean updateDate(int id, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(COL2, date);
        return db.update(TABLE_NAME, args, COL1 + "=" + id, null) > 0;
    }

}


