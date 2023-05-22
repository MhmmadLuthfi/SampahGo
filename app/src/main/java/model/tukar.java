package model;

import com.google.firebase.Timestamp;

public class tukar {
    private String namaPenukaran, deskripsiPenukaran, pointPenukaran, imageUri, alamatPenukaran, nohpPenukaran, penggunaID;
    private Timestamp timeAdded;

    public tukar() {

    }

    public tukar(String namaPenukaran, String deskripsiPenukaran, String pointPenukaran, String alamatPenukaran, String imageUri, String nohpPenukaran, Timestamp timeAdded, String penggunaID) {
        this.namaPenukaran = namaPenukaran;
        this.deskripsiPenukaran = deskripsiPenukaran;
        this.pointPenukaran = pointPenukaran;
        this.alamatPenukaran = alamatPenukaran;
        this.nohpPenukaran = nohpPenukaran;
        this.imageUri = imageUri;
        this.timeAdded = timeAdded;
        this.penggunaID = penggunaID;
    }

    public String getnamaPenukaran() {
        return namaPenukaran;
    }

    public void setnamaPenukaran(String namaPenukaran) {
        this.namaPenukaran = namaPenukaran;
    }

    public String getDeskripsiPenukaran() {
        return deskripsiPenukaran;
    }

    public void setDeskripsiPenukaran(String deskripsiPenukaran) {
        this.deskripsiPenukaran = deskripsiPenukaran;
    }

    public String getPointPenukaran() {
        return pointPenukaran;
    }

    public void setPointPenukaran(String pointPenukaran) {
        this.pointPenukaran = pointPenukaran;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getAlamatPenukaran() {
        return alamatPenukaran;
    }

    public void setAlamatPenukaran(String alamatPenukaran) {
        this.alamatPenukaran = alamatPenukaran;
    }

    public String getNohpPenukaran() {
        return nohpPenukaran;
    }

    public void setNohpPenukaran(String nohpPenukaran) {
        this.nohpPenukaran = nohpPenukaran;
    }

    public String getPenggunaID() {
        return penggunaID;
    }

    public void setPenggunaID(String penggunaID) {
        this.penggunaID = penggunaID;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }
}