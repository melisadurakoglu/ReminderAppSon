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
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements CustomAdapter.OnNoteListener{
    List<Yapilacak> notList = new ArrayList<>();
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    private RecyclerView recyclerView;
    private CustomAdapter mAdapter;
    Button share;
    Button son10Gun,son20Gun,son30Gun;
    TextView textViewStart,textViewFinish;
    int day,month,year;
    String start="01/01/2020";
    String stop="01/02/2020";
    DatabaseReference myRef2;
    public static String id2;
    CheckBox checkBox;
    private int completed;
    String uID;
    FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        myRef2 = FirebaseDatabase.getInstance().getReference().child("yapilacaklar");
        recyclerView = view.findViewById(R.id.recyclerview);
        mAdapter = new CustomAdapter(notList,this );
        checkBox=view.findViewById(R.id.checkbox);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        son10Gun=view.findViewById(R.id.son10Gun);
        son20Gun=view.findViewById(R.id.son20Gun);
        son30Gun=view.findViewById(R.id.son30Gun);
        textViewStart=view.findViewById(R.id.baslangicText);
        textViewFinish=view.findViewById(R.id.bitisText);
        textViewStart.setVisibility(View.GONE);
        textViewFinish.setVisibility(View.GONE);
        son10Gun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);
                textViewStart.setText(day + "/" + (month + 1) + "/" + year);
                start=textViewStart.getText().toString();
                textViewFinish.setText((day-7)+"/"+(month+1)+"/"+year);
                stop=textViewFinish.getText().toString();
                try {
                    startDate = new SimpleDateFormat("dd/MM/yyyy").parse(start);
                    endDate=new SimpleDateFormat("dd/MM/yyyy").parse(stop);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                myRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        notList.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Yapilacak yap = ds.getValue(Yapilacak.class);
                                if (yap != null && tarihler(yap.getTarih())) {
                                    notList.add(yap);
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
                recyclerView.setAdapter(mAdapter);
            }
        });
        son20Gun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);
                textViewStart.setText(day + "/" + (month + 1) + "/" + year);
                start=textViewStart.getText().toString();
                textViewFinish.setText((day-20)+"/"+(month+1)+"/"+year);
                stop=textViewFinish.getText().toString();
                try {
                    startDate = new SimpleDateFormat("dd/MM/yyyy").parse(start);
                    endDate=new SimpleDateFormat("dd/MM/yyyy").parse(stop);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                myRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        notList.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Yapilacak yap = ds.getValue(Yapilacak.class);
                                if (yap != null && tarihler(yap.getTarih())) {
                                    notList.add(yap);
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
                recyclerView.setAdapter(mAdapter);

            }
        });
        son30Gun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);
                textViewStart.setText(day + "/" + (month + 1) + "/" + year);
                start=textViewStart.getText().toString();
                textViewFinish.setText((day-30)+"/"+(month+1)+"/"+year);
                stop=textViewFinish.getText().toString();
                try {
                    startDate = new SimpleDateFormat("dd/MM/yyyy").parse(start);
                    endDate=new SimpleDateFormat("dd/MM/yyyy").parse(stop);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                myRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        notList.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Yapilacak yap = ds.getValue(Yapilacak.class);
                                if (yap != null && tarihler(yap.getTarih())) {
                                    notList.add(yap);
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
                recyclerView.setAdapter(mAdapter);
            }
        });

        return view;

    }
    private boolean tarihler(String notTarih) {
        Date date1=null;
        try {
            date1=new SimpleDateFormat("dd/MM/yyyy").parse(notTarih);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(startDate.compareTo((date1))*(date1).compareTo(endDate)>=0){
            return true;
        }
        return false;
    }
    @Override
    public void onStart() {
        super.onStart();
        //attaching value event listener
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Yapilacak not1 = ds.getValue(Yapilacak.class);
                        if (not1 != null) {
                            notList.add(not1);
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
        mAdapter = new CustomAdapter(notList, (CustomAdapter.OnNoteListener) this);
        recyclerView.setAdapter(mAdapter);
    }
    @Override
    public void onNoteClick(int position) {
        Yapilacak not = notList.get(position);
        Intent intent=new Intent(getActivity(),UpdateActivity.class);
        intent.putExtra("id",not.getId());
        intent.putExtra("yap",not.getYapilacak());
        intent.putExtra("etiket",not.getEtiket());
        intent.putExtra("tarih",not.getTarih());
        intent.putExtra("saat",not.getSaat());
        startActivity(intent);

    }
    Date startDate;

    {
        try {
            startDate = new SimpleDateFormat("dd/MM/yyyy").parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    Date endDate;

    {
        try {
            endDate = new SimpleDateFormat("dd/MM/yyyy").parse(stop);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
