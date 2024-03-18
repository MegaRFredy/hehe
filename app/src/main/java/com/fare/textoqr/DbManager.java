package com.fare.textoqr;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "my_database";
    private static final int DATABASE_VERSION = 1;

    // Table name
    private static final String TABLE_DRIVERS = "drivers";

    // Column names
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_DRIVER_NAME = "Drivername";

    public DbManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table query
        String createTableQuery = "CREATE TABLE " + TABLE_DRIVERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DRIVER_NAME + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVERS);

        // Create tables again
        onCreate(db);
    }

    // Method to save a driver name into the database
    public void saveDriver(String driverName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DRIVER_NAME, driverName);
        // Inserting Row
        db.insert(TABLE_DRIVERS, null, values);
        db.close(); // Closing database connection
    }
}
