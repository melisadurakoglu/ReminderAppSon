package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    Toolbar mToolbar;
    DatabaseReference databaseArtists;
    EditText editTextYapilacak;//Not Başlık
    EditText editTextEtiket;//Not İçerik
    TextView textViewTarih;//Not Tarih
    TextView textViewSaat;//Eklenecek textViewSaat

    Button btnTarih,btnSaat;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    TimePicker alarmTimePicker;
    MenuItem settings;
    int guncelYil,guncelAy,guncelGun,saat,dakika,guncelSaat,guncelDakika,yil,ay,gun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        databaseArtists= FirebaseDatabase.getInstance().getReference("yapilacaklar");
        editTextYapilacak = (EditText) findViewById(R.id.yapilacak);
        editTextEtiket = (EditText) findViewById(R.id.etiket);
        textViewTarih = (TextView) findViewById(R.id.textViewTarih);
        textViewSaat = (TextView) findViewById(R.id.textViewSaat);
        btnTarih = (Button) findViewById(R.id.btnTarih);
        btnSaat = (Button) findViewById(R.id.btnSaat);

        btnTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                gun = calendar.get(Calendar.DAY_OF_MONTH);
                ay = calendar.get(Calendar.MONTH);
                yil = calendar.get(Calendar.YEAR);
                datePickerDialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textViewTarih.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        guncelGun=dayOfMonth;
                        guncelAy=month;
                        guncelYil=year;
                    }
                }, gun, ay, yil);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        btnSaat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar=Calendar.getInstance();
                saat = calendar.get(Calendar.HOUR_OF_DAY);
                dakika = calendar.get(Calendar.MINUTE);
                TimePickerDialog tpd = new TimePickerDialog(AddActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                textViewSaat.setText(hourOfDay + ":" + minute);
                                guncelSaat=hourOfDay;
                                guncelDakika=minute;
                            }
                        }, saat, dakika, true);
                tpd.setButton(TimePickerDialog.BUTTON_POSITIVE, "Seç", tpd);
                tpd.setButton(TimePickerDialog.BUTTON_NEGATIVE, "İptal", tpd);
                tpd.show();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu_kaydet,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.save){
            createNotificationChannel();
            System.out.println("Yıl:"+guncelYil+" Ay:"+guncelAy+" Gün:"+guncelGun+" Saat:"+guncelSaat+" Dakika:"+guncelDakika);
            //Calendar cal=Calendar.getInstance();
            calendar.set(Calendar.YEAR,(guncelYil));
            calendar.set(Calendar.MONTH,(guncelAy+1));
            calendar.set(Calendar.DAY_OF_MONTH,(guncelGun));
            calendar.set(Calendar.HOUR_OF_DAY,(guncelSaat-1));
            calendar.set(Calendar.MINUTE,(guncelDakika));
            AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent=new Intent(AddActivity.this,NotificationReceiver.class);
            PendingIntent pendingIntent=PendingIntent.getBroadcast(AddActivity.this,0,intent,0);
            calendar.setTimeInMillis(System.currentTimeMillis());
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
            long systemTime=System.currentTimeMillis();
            btnNotKaydet();
        }
        return super.onOptionsItemSelected(item);
    }
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name="Reminder App";
            String description="Description";
            int importance= NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel=new NotificationChannel("notify",name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void btnNotKaydet() {

        if (!editTextYapilacak.getText().toString().isEmpty() && !editTextEtiket.getText().toString().isEmpty() ) {
            String ad = editTextYapilacak.getText().toString().trim();
            String icerik = editTextEtiket.getText().toString().trim();

            String tarih = textViewTarih.getText().toString();
            String saat=textViewSaat.getText().toString();
            String id = databaseArtists.push().getKey();
            String notDurum = "0";
            Yapilacak not = new Yapilacak(id, ad, icerik,tarih,saat,notDurum);

            databaseArtists.child(id).setValue(not).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddActivity.this);
                    alertDialog.setTitle("Görev Eklendi");
                    alertDialog
                            .setMessage("Görev ekleme başarılı.")
                            .setCancelable(false)
                            .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    editTextYapilacak.setText("");
                                    editTextEtiket.setText("");
                                    textViewSaat.setText("");
                                    textViewTarih.setText("");
                                    Intent intent=new Intent(AddActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert;
                    alert= alertDialog.create();
                    alert.show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddActivity.this, "Görev ekleme başarısız, tekrar deneyiniz.", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(AddActivity.this, "Lütfen boş alanları kontrol ediniz.", Toast.LENGTH_SHORT).show();
        }

    }


}

