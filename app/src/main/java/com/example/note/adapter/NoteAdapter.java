package com.example.note.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.note.R;
import com.example.note.dao.NoteDAO;
import com.example.note.entity.Note;
import com.example.note.itf.ITFOnClick;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    List<Note> list;
    ITFOnClick itfOnClick;
    ViewBinderHelper binderHelper = new ViewBinderHelper();
    Context context;
    NoteDAO dao;

    public NoteAdapter(List<Note> list, Context context, ITFOnClick itfOnClick) {
        this.list = list;
        this.context = context;
        this.itfOnClick = itfOnClick;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note_rcv, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        Note note = list.get(position);
        if (note == null){
            return;
        }

        holder.tv1.setText(" "+note.getDate().substring(7, note.getDate().length()));
        holder.tv2.setText(" "+note.getTitle());
        holder.tv3.setText(note.getContent());

        holder.tv_xoa.setText("xóa");
        binderHelper.bind(holder.swipLayout, String.valueOf(note.getId()));
        holder.layout_delete.setOnClickListener(view -> {
            if (holder.tv_xoa.getText().equals("xóa")){
                //Toast.makeText(context, "nhấn lại để xóa", Toast.LENGTH_SHORT).show();
                holder.tv_xoa.setText("xác nhận");
                YoYo.with(Techniques.FadeIn).duration(500).repeat(0).playOn(holder.tv_xoa);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.tv_xoa.setText("xóa");
                        YoYo.with(Techniques.FadeIn).duration(500).repeat(0).playOn(holder.tv_xoa);
                    }
                },1500);
            }else if (holder.tv_xoa.getText().equals("xác nhận")){
                dao = new NoteDAO(context);
                if (dao.delete(note.getId()) > 0){
                    list.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                }else {
                    Toast.makeText(context, "không thể xóa, vui lòng thử lại!!!", Toast.LENGTH_SHORT).show();
                    holder.tv_xoa.setText("xóa");
                }
            }else {

            }

        });

        holder.cv.setOnClickListener(view -> {
            itfOnClick.ItemOnClick(note);
        });
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{

        private TextView tv1, tv2, tv3, tv_xoa;
        private CardView cv;
        SwipeRevealLayout swipLayout;
        LinearLayout layout_delete;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv_item_rcv_1);
            tv2 = itemView.findViewById(R.id.tv_item_rcv_2);
            tv3 = itemView.findViewById(R.id.tv_item_rcv_3);
            cv = itemView.findViewById(R.id.cv_item_rcv);
            layout_delete = itemView.findViewById(R.id.layout_del);
            swipLayout = itemView.findViewById(R.id.swip_layout);
            tv_xoa = itemView.findViewById(R.id.tv_xoa);
        }
    }
}
