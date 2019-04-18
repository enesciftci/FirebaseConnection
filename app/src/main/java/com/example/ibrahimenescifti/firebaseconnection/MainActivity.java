package com.example.ibrahimenescifti.firebaseconnection;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
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

    Calendar calendar=Calendar.getInstance();
    Calendar calendar1=Calendar.getInstance();
    int ders=1;
    private String _ad, _soyad, _okulNo, _sinif, _sube, _girisTarihi,_imei,_girisSaati,_girisYapilanSinifNo,_ders;
    EditText ad, soyad, okulNo, sube, sinif;
    TextView label;
    String IMEINumber,GirisYapilanSinifNo;
    Button girisButonu, getirButonu,qrButonu;
    List<Student> studentList = new ArrayList<Student>();
    Map<String, String> studentMap = new HashMap<String, String>();
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    FirebaseFirestore oku = FirebaseFirestore.getInstance();
final Activity activity=this;
    TelephonyManager telephonyManager;
    Student student = new Student(_ad, _soyad, _sinif, _sube, _okulNo, _girisTarihi,_imei,_girisSaati,_girisYapilanSinifNo,_ders);
    private static final int PERMISSION_REQUEST_CODE = 1;
    IntentIntegrator integrator ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
qrButonu=(Button)findViewById(R.id.buttonQR);
qrButonu.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       integrator=new IntentIntegrator(activity);
       integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
       integrator.setPrompt("QR Kodu Tarayın");
       integrator.setCameraId(0);
       integrator.setBeepEnabled(true);
       integrator.setBarcodeImageEnabled(false);
       integrator.initiateScan();
    }
});

        girisButonu = (Button) findViewById(R.id.buttonGiris);
        girisButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                label = (TextView) findViewById(R.id.textViewAd);
                ad = (EditText) findViewById(R.id.editTextAd);
                soyad = (EditText) findViewById(R.id.editTextSoyad);
                okulNo = findViewById(R.id.editTextOkulNo);
                sube = findViewById(R.id.editTextSube);
                sinif = findViewById(R.id.editTextSınıf);
                if (kontrol(ad) || kontrol(soyad) || kontrol(okulNo) || kontrol(sinif) || kontrol(sube)) {
                    try
                    {
                    basicWrite();
                    }
                    catch (Exception e)
                    {
                       e.printStackTrace();
                    }

                }
            }
        });

        getirButonu = (Button) findViewById(R.id.buttonGetir);

        if ((ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)) {

            // Should we show an explanation?
            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_PHONE_STATE))){
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        getirButonu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                imeiOku();
            //    System.out.println(IMEINumber);
                oku.collection("Sınıflar").document("11").collection("A").document(IMEINumber).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            String name = task.getResult().getString("ad");
                            String surname = task.getResult().getString("soyad");
                            String schnumber = task.getResult().getString("okulNo");
                            String field = task.getResult().getString("sube");
                            String regdate = task.getResult().getString("girisTarihi");
                            String imei=task.getResult().getString("imei");

                            System.out.println(name+" "+" "+surname +" "+schnumber+ " "+field+" "+regdate);
                            System.out.println(imeiOku());
                        }
                    }
                });
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
            //label.setText("Sonuç Bulunamadı Tekrar Deneyin");
        }
        else
        {
            System.out.println(result.getContents());
            GirisYapilanSinifNo=result.getContents().toString();
           // label.setText("abc "+result.getContents());
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
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            fileOutputStream.write(tm.getDeviceId().getBytes());
            System.out.println("imei yazıldı"+tm.getDeviceId());
        fileOutputStream.close();
    }catch (Exception e)
    {
        e.printStackTrace();
    }
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
Date dakikaHesapla(int dakika)
{
    Date date1=new Date();
    calendar.add(Calendar.MINUTE,dakika);
    date1=calendar.getTime();
    return date1;
}
Date girisSaati(int dakika)
{
    Date date2;
    calendar1.add(Calendar.MINUTE,dakika);
   // calendar1.set(Calendar.HOUR,);
    date2=calendar1.getTime();
    return date2;
}

    public void basicWrite() throws ParseException {

imeiOku();
try{
        System.out.println("Basicwrite için imei okundu");
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        DateFormat dateFormat1=new SimpleDateFormat("dd/MM/yyyy");

    if(girisSaati(0).getTime()<=dakikaHesapla(40).getTime())
    {
        System.out.println("1.ders");
    }
    else if(girisSaati(50).getTime()<=dakikaHesapla(90).getTime() )
    {
        System.out.println("2.ders");
    }
    else if(girisSaati(100).getTime()<=dakikaHesapla(140).getTime())
    {
        System.out.println("Şuan 3. ders");

    }
    else if(girisSaati(150).getTime()<=dakikaHesapla(190).getTime())
    {
        System.out.println("Şuan 4. ders");

    }
    else if(girisSaati(190).getTime()<=dakikaHesapla(230).getTime())
    {
        System.out.println("Şuan 5. ders");
    }
    Date date = new Date();
        student.setAd(ad.getText().toString().toUpperCase());
        student.setSoyad(soyad.getText().toString().toUpperCase());
        student.setOkulNo(okulNo.getText().toString().toUpperCase());
        student.setSube(sube.getText().toString().toUpperCase());
        student.setDers("1");
        student.setSinif(sinif.getText().toString().toUpperCase());
        student.setGirisYapilanSinifNo(GirisYapilanSinifNo);
        String a=dateFormat1.format(date).toString();
        a=a.replace('/',' ');
        String strWithSpaceTabNewline = a;
        String formattedStr01 = strWithSpaceTabNewline.replaceAll("\\s"," ");
        student.setGirisTarihi(formattedStr01);
        student.setIMEI(IMEINumber.toString());
        student.setGirisSaati(dateFormat.format(date).toString());
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
        studentMap.put(_girisSaati,dateFormat1.format(date));
        studentMap.put(ders+".Ders",_ders);
        studentMap.put("girisYapilanSinifNo",_girisYapilanSinifNo);

        database.collection("Sınıflar").document(_sinif).collection(_sube).document(_girisTarihi).collection("ogrenciler").document(_imei).set(student);// Burada yapı
        // koleksiyon/döküman/koleksiyon/döküman olarak gidiyor sonuç olarak her öğrenci bir döküman haline geliyor.
    }
    catch (Exception e)
    {
       throw e;
    }
    }


    }

