package model;

import com.google.firebase.Timestamp;

public class jemput {
    private String kategori, alamat, tanggal, waktu, imageUri, userId;
    private Timestamp timeAdded;

    public jemput(){

    }
    public jemput(String kategori, String alamat, String tanggal, String waktu, String imageUri, Timestamp timeAdded, String userId){
        this.kategori = kategori;
        this.alamat = alamat;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.imageUri = imageUri;
        this.timeAdded = timeAdded;
        this.userId = userId;
    }
    public String getKategori(){
        return kategori;
    }
    public void setKategori (String kategori){
        this.kategori = kategori;
    }
    public String getAlamat(){
        return alamat;
    }
    public void setAlamat (String alamat){
        this.alamat = alamat;
    }
    public String getTanggal(){
        return tanggal;
    }
    public void setTanggal (String tanggal){
        this.tanggal = tanggal;
    }
    public String getWaktu(){
        return waktu;
    }
    public void setWaktu (String waktu){
        this.waktu = waktu;
    }
    public String getImageUri(){
        return imageUri;
    }
    public void setImageUri(String imageUri){
        this.imageUri = imageUri;
    }
    public Timestamp getTimeAdded() {
        return timeAdded;
    }
    public void setTimeAdded(Timestamp timeAdded){
        this.timeAdded = timeAdded;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
