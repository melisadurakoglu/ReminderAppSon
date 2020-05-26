package com.example.reminderapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter2 extends RecyclerView.Adapter<CustomAdapter2.MyViewHolder> {
    private List<Yapilacak> notList;
    private OnNoteListener mOnNoteListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView hashtag,etiketBaslik;
        public CheckBox checkBox;

        OnNoteListener onNoteListener;

        public MyViewHolder(View v, OnNoteListener onNoteListener) {
            super(v);

            this.onNoteListener = onNoteListener;
            //Düzenlenecek layoutta
            etiketBaslik = v.findViewById(R.id.etiketBaslik);
            hashtag = v.findViewById(R.id.hashtag);
            //Checkbox yeni eklendi.
            checkBox=v.findViewById(R.id.checkbox);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CustomAdapter2(List<Yapilacak> notList, OnNoteListener onNoteListener) {
        this.notList = notList;
        this.mOnNoteListener = onNoteListener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_etiket, parent, false);

        return new MyViewHolder(itemView,mOnNoteListener);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Yapilacak not = notList.get(position);
        holder.etiketBaslik.setText(not.getEtiket());
        holder.hashtag.setText("#");

    }

    @Override
    public int getItemCount() {
        return notList.size();
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}