package com.example.note.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.service.autofill.SaveCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note.R;
import com.example.note.SubClass;
import com.example.note.entity.NoteImage;
import com.example.note.itf.ITFOnClickImageItem;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    List<NoteImage> list;
    Context context;
    ITFOnClickImageItem itf;
    int SPAN;
    public ImageAdapter(Context context, List<NoteImage> list, int span, ITFOnClickImageItem itf) {
        this.context = context;
        this.list = list;
        this.itf = itf;
        this.SPAN = span;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rcv_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        NoteImage image = list.get(position);
        if (image == null){
            return;
        }
        Bitmap bitmap = SubClass.ByteToBitmap(image.getImage());
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float scale = (float)h/w;
        holder.iv_main.setImageBitmap(bitmap);
        holder.iv_del.setOnClickListener(view -> {
            itf.OnClick(position);
        });

        holder.cv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout()  {
                holder.cv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int widthCV = holder.cv.getWidth();
                int heightCV = holder.cv.getHeight();

                holder.tv.setText(""+widthCV);

                ViewGroup.LayoutParams params = holder.iv_main.getLayoutParams();
                params.width = widthCV/SPAN;
                params.height = (int) ((float)widthCV*scale);
                holder.iv_main.setLayoutParams(params);
            }
        });



    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_main, iv_del;
        TextView tv;
        ConstraintLayout layout;
        CardView cv;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_del = itemView.findViewById(R.id.iv_del_item_image);
            iv_main = itemView.findViewById(R.id.iv_item_image);
            tv = itemView.findViewById(R.id.textView3);
            layout = itemView.findViewById(R.id.cst_item_image);
            cv = itemView.findViewById(R.id.cv_image_item);
        }
    }
}
