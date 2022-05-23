package com.example.note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.example.note.adapter.NoteAdapter;
import com.example.note.dao.NoteDAO;
import com.example.note.entity.Note;
import com.example.note.itf.ITFOnClick;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rcv;
    FloatingActionButton fab;
    NoteAdapter adapter;
    List<Note> list;
    NoteDAO noteDAO;
    int SPAN = 1;
    ConstraintLayout main_layout;
    SimpleDateFormat formatAll = new SimpleDateFormat("HH:mm, dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        fab = findViewById(R.id.fab_main);
        rcv = findViewById(R.id.rcv_main);
        main_layout = findViewById(R.id.cst_main_acti);

        fab.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AddNoteActivity.class));
        });
        noteDAO = new NoteDAO(MainActivity.this);
        setRCV();

        main_layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout()  {
                main_layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = main_layout.getWidth();
                int height = main_layout.getHeight();
                //Toast.makeText(MainActivity.this, ""+width+" "+height, Toast.LENGTH_SHORT).show();
                getScreen(width, height);
            }
        });
    }
    public Date TXTtoDate(String txt){
        try {
            return formatAll.parse(txt);
        }catch (Exception e){
            return new Date();
        }
    }
    public void setRCV(){
        list = noteDAO.getAll();
        Collections.sort(list, new Comparator<Note>() {
            @Override
            public int compare(Note t0, Note t1) {
                return TXTtoDate(t1.getDate()).compareTo(TXTtoDate(t0.getDate()));
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, SPAN);
        rcv.setLayoutManager(mLayoutManager);
        rcv.setItemAnimator(new DefaultItemAnimator());

        rcv.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isShown())
                    fab.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    fab.show();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        adapter = new NoteAdapter(list, this, new ITFOnClick() {
            @Override
            public void ItemOnClick(Note note) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("note", note);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        rcv.setAdapter(adapter);
    }

    public void getScreen(int width, int height){
        SharedPreferences pref = getSharedPreferences("SCREEN", MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("W", width);
        edit.putInt("H", height);
        edit.commit();
    }
}