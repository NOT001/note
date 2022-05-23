package com.example.note;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.note.adapter.ImageAdapter;
import com.example.note.dao.NoteDAO;
import com.example.note.dao.NoteImageDAO;
import com.example.note.entity.Note;
import com.example.note.entity.NoteImage;
import com.example.note.itf.ITFOnClickImageItem;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class AddNoteActivity extends AppCompatActivity {

    SimpleDateFormat formatAll = new SimpleDateFormat("HH:mm, dd/MM/yyyy");
    TextView tv1, tv2;
    EditText ed_content, ed_title;
    ImageView iv_save_note, iv_shiw_image, iv_hide;
    NoteDAO noteDAO;
    Note note;
    int count_char = 0;
    FloatingActionButton fab_camera, fab_album;
    LinearLayout layoutParent;
    NoteImageDAO noteImageDAO;
    List<Integer> listID;
    List<NoteImage> noteImageList;
    RecyclerView rcv;
    ImageAdapter imageAdapter;
    ConstraintLayout main_layout;
    int SPAN = 1;
    public static final int REQUEST_CODE_CAMERA = 100;
    public static final int REQUEST_CODE_FOLDER = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        getSupportActionBar().hide();
        tv1 = findViewById(R.id.tv_acti_add_1);
        tv2 = findViewById(R.id.tv_acti_add_2);
        ed_title = findViewById(R.id.ed_acti_add_title);
        ed_content = findViewById(R.id.ed_acti_add_content);
        iv_save_note = findViewById(R.id.iv_save_note);
        fab_album = findViewById(R.id.fab_add_album);
        fab_camera = findViewById(R.id.fab_add_camere);
        rcv = findViewById(R.id.rcv_image_add);
        iv_hide = findViewById(R.id.iv_image_hide);
        main_layout = findViewById(R.id.cst_add_note);

        noteImageList = new ArrayList<>();
        layoutParent = findViewById(R.id.layout_add_liner);
        note = new Note();
        noteDAO = new NoteDAO(AddNoteActivity.this);
        noteImageDAO = new NoteImageDAO(AddNoteActivity.this);

        tv2.setText("0 ký tự");

        Date now = new Date();
        tv1.setText(formatAll.format(now));
        note.setDate(formatAll.format(now));
        ed_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String rs = ed_content.getText().toString();
                if (rs != null){
                    count_char = rs.length();
                    tv2.setText(""+count_char+" ký tự");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        delay5s();
        iv_save_note.setOnClickListener(view -> {
            if (ValContent()==0){
                Toast.makeText(this, "bạn chưa nhập nội dung!!!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (noteDAO.insert(note) > 0){
                Toast.makeText(AddNoteActivity.this, "Đã lưu ghi chú mới", Toast.LENGTH_SHORT).show();
                if (!noteImageList.isEmpty()){
                    for (NoteImage a: noteImageList){
                        a.setIdNote(getMaxID());
                        noteImageDAO.insert(a);
                    }
                }

                startActivity(new Intent(AddNoteActivity.this, MainActivity.class));
            }else {
                Toast.makeText(AddNoteActivity.this, "không thể lưu", Toast.LENGTH_SHORT).show();
            }
        });
        CreateImage();


    }

    public void CreateImage(){
        fab_camera.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CODE_CAMERA);
        });
        fab_album.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_FOLDER);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            iv_hide.setImageBitmap(bitmap);
            NoteImage noteImage = new NoteImage();
            noteImage.setIdNote(-99);
            noteImage.setImage(SubClass.imageViewToByteArray(iv_hide));
            noteImageList.add(noteImage);
            setRCVImage(noteImageList);
        }
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            iv_hide.setImageURI(uri);
            NoteImage noteImage = new NoteImage();
            noteImage.setIdNote(-99);
            noteImage.setImage(SubClass.imageViewToByteArray(iv_hide));
            noteImageList.add(noteImage);
            setRCVImage(noteImageList);
        }
    }


    public void delay5s(){
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Date now = new Date();
            tv1.setText(formatAll.format(now));
            note.setDate(formatAll.format(now));
        }, 5000);
    }

    public void setRCVImage(List<NoteImage> list){
        SharedPreferences pref = getSharedPreferences("SCREEN", MODE_PRIVATE);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, SPAN);
        rcv.setLayoutManager(mLayoutManager);
        rcv.setItemAnimator(new DefaultItemAnimator());
        imageAdapter = new ImageAdapter(AddNoteActivity.this, list, SPAN, new ITFOnClickImageItem() {
            @Override
            public void OnClick(int position) {
                noteImageList.remove(position);
                setRCVImage(noteImageList);
            }
        });
//        if (noteImageList.size() >= 2){
//            SPAN = 2;
//            RecyclerView.LayoutManager m2LayoutManager = new GridLayoutManager(this, SPAN);
//            rcv.setLayoutManager(m2LayoutManager);
//            rcv.setItemAnimator(new DefaultItemAnimator());
//            imageAdapter = new ImageAdapter(AddNoteActivity.this, list, SPAN, new ITFOnClickImageItem() {
//                @Override
//                public void OnClick(int position) {
//                    noteImageList.remove(position);
//                    setRCVImage(noteImageList);
//                }
//            });
//        }
        rcv.setAdapter(imageAdapter);
    }

    public int ValContent(){
        if (ed_content.getText().toString().trim().isEmpty()){
            return 0;
        }
        if (ed_title.getText().toString().trim().isEmpty()){
            //note.setTitle("tiêu đề trống");
        }else {
            note.setTitle(ed_title.getText().toString().trim());
        }
        note.setContent(ed_content.getText().toString().trim());
        return ed_content.getText().toString().trim().length();
    }

    public int getMaxID(){
        listID = new ArrayList<>();
        List<Note> noteList = noteDAO.getAll();
        for (Note note: noteList){
            listID.add(note.getId());
        }
        Collections.sort(listID);
        return listID.get(listID.size() - 1);
    }

}