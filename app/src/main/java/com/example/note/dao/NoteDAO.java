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

public class NoteDAO {
    private SQLiteDatabase db;
    private final String TABLE = "note_p";
    public NoteDAO(Context context){
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insert(Note note){
        ContentValues values = new ContentValues();
        //values.put("id", String.valueOf(note.getId()));
        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        values.put("date", note.getDate());
        return db.insert(TABLE, null, values);
    }

    public int update(Note note){
        ContentValues values = new ContentValues();
        //values.put("id", String.valueOf(note.getId()));
        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        values.put("date", note.getDate());
        return db.update(TABLE, values, "id=?", new String[]{String.valueOf(note.getId())});
    }

    public int delete(int id){
        return db.delete(TABLE, "id=?", new String[]{String.valueOf(id)});
    }

    public List<Note> getAll(){
        String sql = "SELECT * FROM "+TABLE;
        return getData(sql);
    }
    public Note getID(int id){
        String sql = "SELECT * FROM "+TABLE+" WHERE id=?";
        return getData(sql, String.valueOf(id)).get(0);
    }
    @SuppressLint("Range")
    private List<Note> getData(String sql , String...selectionArgs){
        List<Note> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()){
            Note obj = new Note();
            obj.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
            obj.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            obj.setContent(cursor.getString(cursor.getColumnIndex("content")));
            obj.setDate(cursor.getString(cursor.getColumnIndex("date")));
            list.add(obj);
        }
        return list;
    }
}
