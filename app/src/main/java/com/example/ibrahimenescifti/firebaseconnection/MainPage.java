package com.example.ibrahimenescifti.firebaseconnection;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainPage extends AppCompatActivity {
    Calendar calendar=Calendar.getInstance();
Button btnGiris;
TextView sinifNo;
Activity activity=this;
    IntentIntegrator integrator ;
   String GirisYapilanSinifNo;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    Map<String, String> studentMap = new HashMap<String, String>();
    private String _ad, _soyad, _okulNo, _sinif, _sube, _girisTarihi,_imei,_girisSaati,_girisYapilanSinifNo,_ders;
    private String ad,soyad,okulNo,sinif,sube,girisTarihi,imei,girisSaati,girisYapilanSinifNo,ders;
    Student student = new Student(_ad, _soyad, _sinif, _sube, _okulNo, _girisTarihi,_imei,_girisSaati,_girisYapilanSinifNo,_ders);
    DateFormat saatFormat = new SimpleDateFormat("HH:mm");
    DateFormat tarihFormat=new SimpleDateFormat("dd/MM/yyyy");
    Date date=new Date();
    int tarih;
    String formattedStr01;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Intent intent=getIntent();
        ad=intent.getStringExtra(MainActivity.AD);
        soyad=intent.getStringExtra(MainActivity.SOYAD);
        sube=intent.getStringExtra(MainActivity.SUBE);
        okulNo=intent.getStringExtra(MainActivity.OKULNO);
        imei=intent.getStringExtra(MainActivity.IMEI);
        sinif=intent.getStringExtra(MainActivity.SINIF);


        integrator=new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("QR Kodu Tarayın");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
        btnGiris=(Button)findViewById(R.id.buttonSinifaGirisYap);
        btnGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    basicWrite();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()==null)
            {
                System.out.println("Sonuç Bulunamadı");
                //label.setText("Sonuç Bulunamadı Tekrar Deneyin");
            }
            else
            {
                System.out.println(result.getContents());
                GirisYapilanSinifNo=result.getContents().toString();
                _girisYapilanSinifNo=GirisYapilanSinifNo;

                student.setGirisTarihi(formattedStr01);
                // label.setText("abc "+result.getContents());
            }
        }
    }
    public void basicWrite() throws ParseException {


        try{
            student.setAd(ad.toString().toUpperCase());
            student.setSoyad(soyad.toString().toUpperCase());
            student.setOkulNo(okulNo.toString().toUpperCase());
            student.setSube(sube.toString().toUpperCase());
            student.setDers("0");
            student.setSinif(sinif.toString().toUpperCase());
            student.setGirisYapilanSinifNo(GirisYapilanSinifNo);
            student.setIMEI(imei.toString());
            student.setGirisSaati(saatFormat.format(date).toString());
            student.setGirisTarihi(tarihFormat.format(date).toString());
            _ad = student.getAd();
            _soyad = student.getSoyad();
            _okulNo = student.getOkulNo();
            _sube = student.getSube();
            _ders=student.getDers();
            _sinif = student.getSinif();
            _girisYapilanSinifNo=student.getGirisYapilanSinifNo();
            _girisTarihi = student.getGirisTarihi().toString();
            _girisSaati=student.getGirisSaati().toString();
            _imei=student.getIMEI();
            studentMap.put("Ad", _ad);
            studentMap.put("Soyad", _soyad);
            studentMap.put("OkulNo", _okulNo);
            studentMap.put("Sube", _sube);
            studentMap.put("Sinif", _sinif);
            studentMap.put("GirisTarihi", _girisTarihi);
            studentMap.put("IMEI",_imei);
            studentMap.put(_girisSaati,saatFormat.format(date));
            studentMap.put(String.format("Ders",ders),_ders);
            studentMap.put("girisYapilanSinifNo",_girisYapilanSinifNo);
            String a=_girisTarihi.toString();
            a=a.replace('/',' ');
            String strWithSpaceTabNewline = a;
            formattedStr01 = strWithSpaceTabNewline.replaceAll("\\s"," ");
            database.collection("Sınıflar").document(_sinif).collection(_sube).document(formattedStr01).collection("ogrenciler").document(_imei).set(student);// Burada yapı

        }
        catch (Exception e)
        {
            throw e;
        }
    }
}
