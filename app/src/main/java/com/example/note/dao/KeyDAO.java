package com.example.note.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.note.data.DbHelper;
import com.example.note.entity.Note;

import java.util.ArrayList;
import java.util.List;

public class KeyDAO {
    private SQLiteDatabase db;
    private final String TABLE = "login";
    public KeyDAO(Context context){
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }


}
