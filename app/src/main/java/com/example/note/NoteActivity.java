package com.example.note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.note.adapter.ImageAdapter;
import com.example.note.dao.NoteDAO;
import com.example.note.dao.NoteImageDAO;
import com.example.note.entity.Note;
import com.example.note.entity.NoteImage;
import com.example.note.itf.ITFOnClickImageItem;

import java.util.ArrayList;
import java.util.List;

public class NoteActivity extends AppCompatActivity {

    Note note;
    TextView tv1, tv2;
    EditText ed_content, ed_title;
    ImageView iv_save_note;
    NoteDAO noteDAO;
    NoteImageDAO noteImageDAO;
    List<NoteImage> listImage;
    RecyclerView rcv;
    ImageAdapter adapter;
    List<Integer> listIdImageInsert, listIdImageRemove;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        note = (Note) intent.getSerializableExtra("note");
        tv1 = findViewById(R.id.tv_acti_note_1);
        tv2 = findViewById(R.id.tv_acti_note_2);
        ed_title = findViewById(R.id.ed_acti_note_title);
        ed_content = findViewById(R.id.ed_acti_note_content);
        iv_save_note = findViewById(R.id.iv_save_note2);
        rcv = findViewById(R.id.rcv_note_acti);
        noteDAO = new NoteDAO(NoteActivity.this);
        noteImageDAO = new NoteImageDAO(NoteActivity.this);
        listIdImageInsert = new ArrayList<>();
        listIdImageRemove = new ArrayList<>();
        setData();
        setRCV(0);
        Save();
    }
    public void Save(){
        iv_save_note.setOnClickListener(view -> {
            if (listIdImageRemove.size() > 0){
                for (int i = 0;i<listIdImageRemove.size();i++){
                    if (noteImageDAO.delete(listIdImageRemove.get(i)) <= 0){
                        Toast.makeText(NoteActivity.this, "cccccccccccccccc", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void setData(){
        tv1.setText(note.getDate());
        tv2.setText(""+note.getContent().length()+" ký tự");
        if (note.getTitle() != null){
            ed_title.setText(note.getTitle());
        }
        ed_content.setText(note.getContent());


    }
    public void setRCV(int type){
        if (type == 0){
            listImage = noteImageDAO.getImageOfNote(note.getId());
        }
        adapter = new ImageAdapter(NoteActivity.this, listImage, 1, new ITFOnClickImageItem() {
            @Override
            public void OnClick(int position) {
                listIdImageRemove.add(listImage.get(position).getId());
                listImage.remove(position);
                setRCV(1);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        rcv.setLayoutManager(mLayoutManager);
        rcv.setItemAnimator(new DefaultItemAnimator());
        rcv.setAdapter(adapter);
    }
}