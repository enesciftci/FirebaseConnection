package com.example.ibrahimenescifti.firebaseconnection;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.nfc.NdefMessage;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.opencensus.common.Clock;

public class MainActivity extends AppCompatActivity {
    public final static String AD="com.example.ibrahimenescifti.firebaseconnection.AD";
    public final static String SOYAD="com.example.ibrahimenescifti.firebaseconnection.SOYAD";
    public final static String OKULNO="com.example.ibrahimenescifti.firebaseconnection.OKULNO";
    public final static String SUBE="com.example.ibrahimenescifti.firebaseconnection.SUBE";
    public final static String IMEI="com.example.ibrahimenescifti.firebaseconnection.IMEI";
    public final static String SINIF="com.example.ibrahimenescifti.firebaseconnection.SINIF";
    private String _ad, _soyad, _okulNo, _sinif, _sube;
    EditText ad, soyad, okulNo, sube, sinif;
    String IMEINumber;
    Button girisButonu;
  List<String> bilgiler=new ArrayList<>();
    TelephonyManager telephonyManager;
   // Student student = new Student(_ad, _soyad, _sinif, _sube, _okulNo, _girisTarihi,_imei,_girisSaati,_girisYapilanSinifNo,_ders);
    private static final int PERMISSION_REQUEST_CODE = 1;
View v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if (bilgileriOku() == true) {
                bilgileriOku();
            }
        else {
        girisButonu = (Button) findViewById(R.id.buttonGiris);
        girisButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ad = (EditText) findViewById(R.id.editTextAd);
                soyad = (EditText) findViewById(R.id.editTextSoyad);
                okulNo = findViewById(R.id.editTextOkulNo);
                sube = findViewById(R.id.editTextSube);
                sinif = findViewById(R.id.editTextSınıf);
                if (kontrol(ad) || kontrol(soyad) || kontrol(okulNo) || kontrol(sinif) || kontrol(sube)) {
                    try {
                        _ad = ad.getText().toString().trim().toUpperCase();
                        System.out.println(_ad);
                        _soyad = soyad.getText().toString().trim().toUpperCase();
                        _okulNo = okulNo.getText().toString().trim();
                        _sinif = sinif.getText().toString().trim();
                        _sube = sube.getText().toString().trim().toUpperCase();
                         bilgiler.add(ad.getText().toString());
                         bilgiler.add(_soyad);
                         bilgiler.add(_okulNo);
                         bilgiler.add(_sinif);
                         bilgiler.add(_sube);
                        imeiOku();
                        VeriGonder(v);
                        bilgileriYaz(_ad);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }}
catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if ((ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_PHONE_STATE))){
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    telephonyManager.getDeviceId();
                    imeiYaz(telephonyManager);
                    // permission was granted, yay!

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    void imeiYaz(TelephonyManager tm) {
        try {
            FileOutputStream fileOutputStream = openFileOutput("imeiNumber", Context.MODE_PRIVATE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            fileOutputStream.write(tm.getDeviceId().getBytes());
        fileOutputStream.close();
    }catch (Exception e)
    {
        e.printStackTrace();
    }
}
void bilgileriYaz(String ad) throws IOException, JSONException {
try{
  FileOutputStream fileOutputStream=openFileOutput("studentInformation",Context.MODE_PRIVATE);
  BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(fileOutputStream));
    for (String b:bilgiler) {

      bufferedWriter.write(b);
      bufferedWriter.newLine();
  }
bufferedWriter.close();
  fileOutputStream.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
}
boolean bilgileriOku() throws FileNotFoundException {
boolean durum=false;

    try {

        FileInputStream fileInputStream=openFileInput("studentInformation");

        InputStreamReader ınputStreamReader=new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader=new BufferedReader(ınputStreamReader);

      _ad=bufferedReader.readLine();
     _soyad=bufferedReader.readLine();
     _okulNo=bufferedReader.readLine();
     _sinif=bufferedReader.readLine();
     _sube=bufferedReader.readLine();
     VeriGonder(v);
        durum=true;

    } catch (Exception e) {
        e.printStackTrace();
        durum=false;
    }
    return durum;
}

String imeiOku()
{
    try {
        FileInputStream fileInputStream=openFileInput("imeiNumber");

        InputStreamReader ınputStreamReader=new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader=new BufferedReader(ınputStreamReader);
       IMEINumber=bufferedReader.readLine();
        System.out.println("İmei okundu"+IMEINumber);


    }catch (Exception e)
    {
        e.printStackTrace();
    }
   return IMEINumber;
}
    boolean kontrol(EditText editText)//Text alan kontrolü
    {
        if (TextUtils.isEmpty(editText.getText())) {
            editText.setError("Boş Geçemezsiniz.");
            return false;
        } else {
            return true;
        }
    }
public void VeriGonder(View v)
{
    imeiOku();
    Intent intent=new Intent(this,MainPage.class);
    intent.putExtra(AD,_ad);
    intent.putExtra(SOYAD,_soyad);
    intent.putExtra(OKULNO,_okulNo);
    intent.putExtra(SINIF,_sinif);
    intent.putExtra(SUBE,_sube);
    intent.putExtra(IMEI,IMEINumber);
    startActivity(intent);
    finish();
}




    }

