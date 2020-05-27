package com.example.reminderapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private List<Yapilacak> notList;

    private OnNoteListener mOnNoteListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView notName,notIcerik,notTarih,notSaat,notDurumu;
        public CheckBox checkBox;

        OnNoteListener onNoteListener;

        public MyViewHolder(View v, OnNoteListener onNoteListener) {
            super(v);

            this.onNoteListener = onNoteListener;

            notName = v.findViewById(R.id.notName);
            notIcerik = v.findViewById(R.id.notIcerik);
            notTarih = v.findViewById(R.id.notTarih);
            notSaat=v.findViewById(R.id.notSaat);
            notDurumu = v.findViewById(R.id.durum);
            checkBox=v.findViewById(R.id.checkbox);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CustomAdapter(List<Yapilacak> notList, OnNoteListener onNoteListener) {
        this.notList = notList;
        this.mOnNoteListener = onNoteListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_item, parent, false);

        return new MyViewHolder(itemView,mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Yapilacak not = notList.get(position);
        holder. notName.setText(not.getYapilacak());
        holder.notIcerik.setText("#"+not.getEtiket());
        holder.notTarih.setText(not.getTarih());
        holder.notSaat.setText(not.getSaat());

        holder.checkBox.setClickable(false);

        if (not.getDurum().equals("1")){

            holder.checkBox.setChecked(true);

        }
        else{

            holder.checkBox.setChecked(false);

        }
    }

    @Override
    public int getItemCount() {
        return notList.size();
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}