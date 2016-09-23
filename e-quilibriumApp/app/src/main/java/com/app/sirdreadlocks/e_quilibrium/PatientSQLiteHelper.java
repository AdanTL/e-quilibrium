package com.app.sirdreadlocks.e_quilibrium;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by adan on 9/23/16.
 */

public class PatientSQLiteHelper extends SQLiteOpenHelper {

    String sqlCreate = "CREATE TABLE Patient (ID TEXT, Surname TEXT, Name TEXT)";

    public PatientSQLiteHelper(Context context, String name,
                                SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Patient");
        db.execSQL(sqlCreate);
    }
}
