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
            //Düzenlenecek layoutta
            notName = v.findViewById(R.id.notName);
            notIcerik = v.findViewById(R.id.notIcerik);
            notTarih = v.findViewById(R.id.notTarih);
            notSaat=v.findViewById(R.id.notSaat);
            notDurumu = v.findViewById(R.id.durum);
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
        //holder.notDurumu.setText(not.getDurum());

        holder.checkBox.setClickable(false);

       /* if(not.getDurum().equals("") && not.getSaat().equals("") && not.getTarih().equals("") && not.getYapilacak().equals("")){
            holder.notIcerik.setVisibility(View.GONE);
            holder.notTarih.setVisibility(View.GONE);
            holder.notSaat.setVisibility(View.GONE);
            holder.notName.setVisibility(View.GONE);
        }
*/





        if (not.getDurum().equals("1")){
            //holder.notDurumu.setText("Görev Tamamlandı");
            holder.checkBox.setChecked(true);
            //holder.notDurumu.setTextColor(Color.parseColor("#008000"));
        }
        else{
            //holder.notDurumu.setText("Görev Tamamlanmadı");
            holder.checkBox.setChecked(false);
            //holder.notDurumu.setTextColor(Color.parseColor("#FF0000"));
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