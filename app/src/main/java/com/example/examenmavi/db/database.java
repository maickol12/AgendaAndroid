package com.example.examenmavi.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class database extends SQLiteOpenHelper {
    public database(Context context,String name,SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        Log.d("agenda","**************");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("" +
                "CREATE TABLE tblAgenda(" +
                    "idAgenda INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "vNombre TEXT NULL," +
                    "vTelefono TEXT NULL," +
                    "vCumpleanios TEXT NULL," +
                    "vNota TEXT NULL" +
                ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
