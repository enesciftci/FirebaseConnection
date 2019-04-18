package com.example.ibrahimenescifti.firebaseconnection;

/**
 * Created by İbrahim Enes Çiftçi on 10.04.2019.
 */

public class Student {
    private String Ad="a";
    private String Soyad="a";
    private String Sube="a";
    private String Sinif="a";
    private String OkulNo="a";
    private String GirisTarihi="01.01.1999";
    private String IMEI="00000";
    private String GirisSaati="00:00";

    public String getGirisYapilanSinifNo() {
        return GirisYapilanSinifNo;
    }

    public void setGirisYapilanSinifNo(String girisYapilanSinifNo) {
        GirisYapilanSinifNo = girisYapilanSinifNo;
    }

    private String GirisYapilanSinifNo="111";

    public String getDers() {
        return Ders;
    }

    public void setDers(String ders) {
        Ders = ders;
    }

    private String Ders="0";



    public Student()
    {

    }

    public String getGirisSaati() {
        return GirisSaati;
    }

    public void setGirisSaati(String girisSaati) {
        GirisSaati = girisSaati;
    }

    public Student(String ad, String soyad, String sube, String sinif, String okulNo, String girisTarihi, String imei, String girisSaati,String ders,String girisYapilanSinifNo) {
        Ad = ad;
        Soyad = soyad;
        Sube = sube;
        Sinif=sinif;
        OkulNo = okulNo;
        GirisTarihi = girisTarihi;
        GirisSaati=girisSaati;
        Ders=ders;
IMEI=imei;
GirisYapilanSinifNo=girisYapilanSinifNo;
    }
    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }
    public String getGirisTarihi() {
        return GirisTarihi;
    }

    public void setGirisTarihi(String girisTarihi) {
        GirisTarihi = girisTarihi;
    }

    public String getAd() {
        return Ad;
    }

    public void setAd(String ad) {
        Ad = ad;
    }

    public String getSoyad() {
        return Soyad;
    }

    public void setSoyad(String soyad) {
        Soyad = soyad;
    }
    public String getSinif() {
        return Sinif;
    }

    public void setSinif(String sinif) {
        Sinif = sinif;
    }

    public String getSube() {
        return Sube;
    }

    public void setSube(String sube) {
        Sube = sube;
    }

    public String getOkulNo() {
        return OkulNo;
    }

    public void setOkulNo(String okulNo) {
        OkulNo = okulNo;
    }
}
