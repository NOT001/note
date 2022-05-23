package com.example.note.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public static int VERSION = 1;
    public static String NAME = "note";
    public DbHelper(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String create0 = "CREATE TABLE note_p" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "content TEXT, " +
                "date TEXT)";
        db.execSQL(create0);

        String create1 = "CREATE TABLE note_image" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idNote INTEGER NOT NULL, " +
                "image BLOB NOT NULL)";
        db.execSQL(create1);

        String create2 = "CREATE TABLE login" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "pass TEXT)";
        db.execSQL(create2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("drop table if exists note_p");
        db.execSQL("drop table if exists login");
    }
}
