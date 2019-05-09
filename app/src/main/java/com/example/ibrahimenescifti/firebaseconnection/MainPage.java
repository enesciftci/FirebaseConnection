package com.example.ibrahimenescifti.firebaseconnection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
Button btnGiris,btnOku;
TextView sinifNo;
    TextView textViewInfo;
Activity activity=this;
    IntentIntegrator integrator ;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    Map<String, String> studentMap = new HashMap<String, String>();
    private String _ad, _soyad, _okulNo, _sinif, _sube, _girisTarihi,_imei,_girisSaati,_girisYapilanSinifNo,_dersAraligi;
    private String ad,soyad,okulNo,sinif,sube,girisTarihi,imei,girisSaati,girisYapilanSinifNo, nfcEtiketi,girisYontemi;
    Student student = new Student(_ad, _soyad, _sinif, _sube, _okulNo, _girisTarihi,_imei,_girisSaati,_girisYapilanSinifNo,_dersAraligi);
    DateFormat saatFormat = new SimpleDateFormat("HH:mm");
    DateFormat tarihFormat=new SimpleDateFormat("dd/MM/yyyy");
    Date date=new Date();
    int tarih;
    String formattedStr01;
    Context context=this;
    NfcAdapter nfcAdapter;
TextView sonSinif;

    Map<String,String>NFCTags=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        sonSinif=(TextView)findViewById(R.id.textViewSonSinif);
Intent intent=getIntent();
        ad=intent.getStringExtra(MainActivity.AD);
        soyad=intent.getStringExtra(MainActivity.SOYAD);
        sube=intent.getStringExtra(MainActivity.SUBE);
        okulNo=intent.getStringExtra(MainActivity.OKULNO);
        imei=intent.getStringExtra(MainActivity.IMEI);
        sinif=intent.getStringExtra(MainActivity.SINIF);
        nfcEtiketi =intent.getStringExtra(MainActivity.NFCETIKETI);
        girisYontemi=intent.getStringExtra(MainActivity.GIRISYONTEMI);
        if(girisYontemi.equals("0")) {
            NFCTags.put(nfcEtiketi, "274");
        }
        System.out.println("Giris Yöntemi " +girisYontemi);
if(girisYontemi.equals("1")) {
    integrator = new IntentIntegrator(activity);
    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
    integrator.setPrompt("QR Kodu Tarayın");
    integrator.setCameraId(0);
    integrator.setBeepEnabled(true);
    integrator.setBarcodeImageEnabled(false);
    integrator.initiateScan();
}
        btnGiris=(Button)findViewById(R.id.buttonSinifaGirisYap);
        btnGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if(_girisYapilanSinifNo!="") {

                        basicWrite();
                    }
                    else {
                       Toast.makeText(context,"Lütfen NFC'yi Okutun",Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }


   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()==null)
            {
                System.out.println("Sonuç Bulunamadı");

            }
            else
            {
                System.out.println(result.getContents());
                girisYapilanSinifNo=result.getContents().toString();

            }
        }
    }

    public void basicWrite() throws ParseException {


        try{
            student.setAd(ad.toString().toUpperCase());
            student.setSoyad(soyad.toString().toUpperCase());
            student.setOkulNo(okulNo.toString().toUpperCase());
            student.setSube(sube.toString().toUpperCase());
            student.setDers(dersAraligi()); // Ders saati tespiti sırasında dataseti dolduruyoruz bu nedenle vazgeçildi.
            student.setSinif(sinif.toString().toUpperCase());
            if(girisYontemi.equals("0")) {
                student.setGirisYapilanSinifNo(NFCTags.get(nfcEtiketi));
            }
            else
            {
                student.setGirisYapilanSinifNo(girisYapilanSinifNo);
            }
            student.setIMEI(imei.toString());
            student.setGirisSaati(saatFormat.format(date).toString());
            student.setGirisTarihi(tarihFormat.format(date).toString());
            _ad = student.getAd();
            _soyad = student.getSoyad();
            _okulNo = student.getOkulNo();
            _sube = student.getSube();
            _dersAraligi=student.getDers();
            _sinif = student.getSinif();
            _girisYapilanSinifNo=student.getGirisYapilanSinifNo();
            _girisTarihi = student.getGirisTarihi().toString();
            _girisSaati=student.getGirisSaati().toString();
            _imei=student.getIMEI();
            studentMap.put("ad", _ad);
            studentMap.put("soyad", _soyad);
            studentMap.put("okulNo", _okulNo);
            studentMap.put("sube", _sube);
            studentMap.put("sinif", _sinif);
            studentMap.put("girisTarihi", _girisTarihi);
            studentMap.put("IMEI",_imei);
            studentMap.put("girisSaati",saatFormat.format(date));
            studentMap.put("girisYapilanSinifNo",_girisYapilanSinifNo);
            switch (dersAraligi())
            {
                case "0":
                {
                    Toast.makeText(getApplicationContext(),"Ders Dışı",Toast.LENGTH_SHORT).show();
                    break;
                }
                case "1":
                {
                    studentMap.put("1.Ders","VAR");
                    break;
                }
                case "2":
                {
                    studentMap.put("2.Ders","VAR");
                    break;
                }
                case "3":
                {
                    studentMap.put("3.Ders","VAR");
                    break;
                }
                case "4":
                {
                    studentMap.put("4.Ders","VAR");
                    break;
                }
                case "5":
                {
                    studentMap.put("5.Ders","VAR");
                    break;
                }
                case "6":
                {
                    studentMap.put("6.Ders","VAR");
                    break;
                }
                case "7":
                {
                    studentMap.put("7.Ders","VAR");
                    break;
                }
                case "8":
                {
                    studentMap.put("8.Ders","VAR");
                    break;
                }
                case "9":
                {
                    studentMap.put("9.Ders","VAR");
                    break;
                }
                case "10":
                {
                    studentMap.put("10.Ders","VAR");
                    break;
                }
            }

            String a=_girisTarihi.toString();
            a=a.replace('/',' ');
            String strWithSpaceTabNewline = a;
            formattedStr01 = strWithSpaceTabNewline.replaceAll("\\s"," ");
            database.collection("Sınıflar").document(_sinif).collection(_sube).document(formattedStr01).collection("ogrenciler").document(_imei).set(studentMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    sonSinif.setText(_girisYapilanSinifNo);
                }
            });

            System.out.println("Giriş Yapılan Sınıf: "+_girisYapilanSinifNo);
        }
        catch (Exception e)
        {
            throw e;
        }
    }
    String dersAraligi() {

        String dersSaati="0";
        Date date = new Date();
        Date date1 = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        Long suan = date.getTime();
        date1.setHours(13);
        date1.setMinutes(30);
        date1.setSeconds(0);
        Long oglenKontrolu = date1.getTime();
        Long baslangicSaati;
        if (suan <= oglenKontrolu) {
            date.setHours(8);
            date.setMinutes(30);
            date.setSeconds(0);
            baslangicSaati = date.getTime();
            if (suan > baslangicSaati && suan < (baslangicSaati + 2400000)) {
                dersSaati = "1";
                System.out.println(dersSaati + ". Ders");
            } else if ((suan > baslangicSaati + 3000000) && suan < (baslangicSaati + 2400000 + 3000000)) {
                dersSaati = "2";
                System.out.println(dersSaati + ". Ders");
            } else if ((suan > baslangicSaati + 6000000) && suan < (baslangicSaati + 2400000 + 6000000)) {
                dersSaati = "3";
                System.out.println(dersSaati + ". Ders");
            } else if ((suan > baslangicSaati + 9000000) && suan < (baslangicSaati + 2400000 + 9000000)) {
                dersSaati = "4";
                System.out.println(dersSaati + ". Ders");
            } else if ((suan > baslangicSaati + 12000000) && suan < (baslangicSaati + 2400000 + 12000000)) {
                dersSaati = "5";
                System.out.println(dersSaati + ". Ders");
            }
        } else {

            date.setHours(13);
            date.setMinutes(30);
            date.setSeconds(0);
            baslangicSaati = date.getTime();
            if (suan > baslangicSaati && suan < (baslangicSaati + 2400000)) {
                dersSaati = "6";
                System.out.println(dersSaati + ". Ders");
            } else if ((suan > baslangicSaati + 3000000) && suan < (baslangicSaati + 2400000 + 3000000)) {
                dersSaati = "7";
                System.out.println(dersSaati + ". Ders");
            } else if ((suan > baslangicSaati + 6000000) && suan < (baslangicSaati + 2400000 + 6000000)) {
                dersSaati = "8";
                System.out.println(dersSaati + ". Ders");
            } else if ((suan > baslangicSaati + 9000000) && suan < (baslangicSaati + 2400000 + 9000000)) {
                dersSaati = "9";
                System.out.println(dersSaati + ". Ders");
            }
            else if ((suan > baslangicSaati + 12000000) && suan < (baslangicSaati + 2400000 + 12000000)) {
                dersSaati = "5";
                System.out.println(dersSaati + ". Ders");
            }
            else
            {
                System.out.println("Ders dışı");
            }

        }
        return dersSaati;
    }
}
