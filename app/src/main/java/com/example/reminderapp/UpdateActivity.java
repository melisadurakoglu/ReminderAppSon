package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {
    String sessionId,sessionYap,sessionEtiket,sessionTarih,sessionSaat;
    EditText editTextName,spinnerGenre;
    TextView textViewTarihUpdate,textViewSaatUpdate,textViewNotDurumu;
    Button buttonUpdate,buttonDelete,buttonTarih,buttonSaat,buttonShare;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        sessionId=getIntent().getStringExtra("id");
        sessionYap=getIntent().getStringExtra("yap");
        sessionEtiket=getIntent().getStringExtra("etiket");
        sessionTarih=getIntent().getStringExtra("tarih");
        sessionSaat=getIntent().getStringExtra("saat");

        editTextName = findViewById(R.id.yapilacak);
        editTextName.setText(sessionYap);

        spinnerGenre =  findViewById(R.id.etiket);
        spinnerGenre.setText(sessionEtiket);
        textViewTarihUpdate = findViewById(R.id.textViewTarih);
        textViewTarihUpdate.setText(sessionTarih);
        textViewSaatUpdate=findViewById(R.id.textViewSaat);
        textViewSaatUpdate.setText(sessionSaat);
        textViewNotDurumu=findViewById(R.id.durum);
        //buttonUpdate = findViewById(R.id.buttonUpdateNot);
        buttonDelete = findViewById(R.id.btnSil);
        buttonTarih  = findViewById(R.id.btnTarih);
        buttonSaat   = findViewById(R.id.btnSaat);
        buttonShare  = findViewById(R.id.btnPaylas);
        buttonTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(UpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textViewTarihUpdate.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                    }
                }, day,month,year);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        buttonSaat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(UpdateActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // hourOfDay ve minute değerleri seçilen saat değerleridir.
                                // Edittextte bu değerleri gösteriyoruz.
                                textViewSaatUpdate.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, true);
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                timePickerDialog.show();
            }
        });
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=editTextName.getText().toString();
                String description=spinnerGenre.getText().toString();
                String message=description.concat(" \n Reminder App ile gönderildi.");
                Intent sharingIntent=new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,name);
                sharingIntent.putExtra(Intent.EXTRA_TEXT,message);

                startActivity(Intent.createChooser(sharingIntent,"Share Using"));
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteNot(sessionId);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu_guncelle,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.update){
            String name = editTextName.getText().toString().trim();
            String genre = spinnerGenre.getText().toString().trim();
            String tarih = textViewTarihUpdate.getText().toString().trim();
            String saat=textViewSaatUpdate.getText().toString().trim();
            String notDurumu;

            CheckBox checkBox = findViewById(R.id.updateCheckbox);
            if(checkBox.isChecked())  notDurumu = "1"; else notDurumu = "0";
            /*
            CheckBox checkBox = findViewById(R.id.checkbox);
            if(checkBox.isChecked()){
                textViewNotDurumu.setText("Görev Tamamlandı");
                textViewNotDurumu.setTextColor(Color.parseColor("#008000"));
            }
            else{
                textViewNotDurumu.setText("Görev Tamamlanmadı");
                textViewNotDurumu.setTextColor(Color.parseColor("#FF0000"));
            }


             */
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(genre)) {
                updateNot(sessionId, name, genre,tarih,saat,notDurumu);

            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean updateNot(String id, String name, String icerik, String tarih, String saat, String notDurumu) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference().child("yapilacaklar").child(id);
        Yapilacak artist = new Yapilacak(id, name, icerik,tarih,saat,notDurumu);
        dR.setValue(artist);
        Toast.makeText(UpdateActivity.this, "Not Güncellendi.", Toast.LENGTH_LONG).show();
        Intent intent=new Intent(UpdateActivity.this,MainActivity.class);
        startActivity(intent);
        return true;
    }
    private boolean deleteNot(String id) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("yapilacaklar").child(id);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
                Toast.makeText(UpdateActivity.this, "Not silindi.", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(UpdateActivity.this,MainActivity.class);
                startActivity(intent);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UpdateActivity.this, "Hata!.\n"+databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        return true;
    }

}
