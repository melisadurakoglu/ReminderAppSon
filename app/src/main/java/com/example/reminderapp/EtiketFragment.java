package com.example.reminderapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EtiketFragment extends Fragment  implements CustomAdapter2.OnNoteListener{
    List<Yapilacak> notList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomAdapter2 mAdapter;
    DatabaseReference dbReference;
    CheckBox checkBox;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_etiket, container, false);
        dbReference = FirebaseDatabase.getInstance().getReference().child("yapilacaklar");
        recyclerView = view.findViewById(R.id.recyclerview2);
        mAdapter = new CustomAdapter2(notList,this);
        checkBox=view.findViewById(R.id.checkbox);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        return view;

    }
    @Override
    public void onStart() {
        super.onStart();
        //attaching value event listener
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Yapilacak yapilacakGorev = ds.getValue(Yapilacak.class);
                        if (yapilacakGorev != null) {
                            notList.add(yapilacakGorev);
                            ds.getKey();
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });
        mAdapter = new CustomAdapter2(notList, (CustomAdapter2.OnNoteListener) this);
        recyclerView.setAdapter(mAdapter);
    }
    @Override
    public void onNoteClick(int position) {
        Yapilacak not = notList.get(position);

    }

}
