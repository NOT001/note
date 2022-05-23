package com.example.note.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.note.data.DbHelper;
import com.example.note.entity.Note;
import com.example.note.entity.NoteImage;

import java.util.ArrayList;
import java.util.List;

public class NoteImageDAO {
    private SQLiteDatabase db;
    private final String TABLE = "note_image";
    public NoteImageDAO(Context context){
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insert(NoteImage image){
        ContentValues values = new ContentValues();
        values.put("idNote", String.valueOf(image.getIdNote()));
        values.put("image", image.getImage());
        return db.insert(TABLE, null, values);
    }

    public int update(NoteImage image){
        ContentValues values = new ContentValues();
        values.put("idNote", String.valueOf(image.getIdNote()));
        values.put("image", image.getImage());
        return db.update(TABLE, values, "id=?", new String[]{String.valueOf(image.getId())});
    }
    public int delete(int id){
        return db.delete(TABLE, "id=?", new String[]{String.valueOf(id)});
    }

    public List<NoteImage> getImageOfNote(int id_note){
        String sql = "SELECT * FROM "+TABLE+" WHERE idNote=?";
        return getData(sql, String.valueOf(id_note));
    }

    public NoteImage getID(int id){
        String sql = "SELECT * FROM "+TABLE+" WHERE id=?";
        return getData(sql, String.valueOf(id)).get(0);
    }

    @SuppressLint("Range")
    private List<NoteImage> getData(String sql , String...selectionArgs){
        List<NoteImage> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()){
            NoteImage obj = new NoteImage();
            obj.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
            obj.setIdNote(Integer.parseInt(cursor.getString(cursor.getColumnIndex("idNote"))));
            obj.setImage(cursor.getBlob(cursor.getColumnIndex("image")));
            list.add(obj);
        }
        return list;
    }
}
