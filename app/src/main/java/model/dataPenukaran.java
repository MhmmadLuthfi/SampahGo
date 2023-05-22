package model;

import android.content.Context;

import java.util.List;

public class dataPenukaran {
    String NamaPenukaran;
    String DeskripsiPenukaran;
    String PointPenukaran;
    String ImageUri;
    String PenukaranID;

    public String getNamaPenukaran() {
        return NamaPenukaran;
    }
    public void setNamaPenukaran(String NamaPenukaran) {
        this.NamaPenukaran = NamaPenukaran;
    }

    public String getDeskripsiPenukaran() {
        return DeskripsiPenukaran;
    }
    public void setDeskripsiPenukaran(String DeskripsiPenukaran) {this.DeskripsiPenukaran = DeskripsiPenukaran;}

    public String getPointPenukaran() {
        return PointPenukaran;
    }
    public void setPointPenukaran(String PointPenukaran) {
        this.PointPenukaran = PointPenukaran;
    }

    public String getPenukaranID() {
        return PenukaranID;
    }
    public void setPenukaranID(String PenukaranID) {
        this.PenukaranID = PenukaranID;
    }

    public String getImageUri() {return ImageUri;}
    public void setImageUri(String ImageUri) {
        this.ImageUri = ImageUri;
    }

    public dataPenukaran(String NamaPenukaran, String DeskripsiPenukaran, String PointPenukaran, String ImageUri) {
        this.NamaPenukaran = NamaPenukaran;
        this.DeskripsiPenukaran = DeskripsiPenukaran;
        this.PointPenukaran = PointPenukaran;
        this.ImageUri = ImageUri;
    }
    public dataPenukaran(){

    }
}
