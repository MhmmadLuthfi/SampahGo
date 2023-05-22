package model;

public class lokasi {
    String NamaLokasi;
    String AlamatLokasi;
    String NohpLokasi;
    String LinkLokasi;

    public String getNamaLokasi() {
        return NamaLokasi;
    }
    public void setNamaLokasi(String NamaLokasi) {
        this.NamaLokasi = NamaLokasi;
    }

    public String getAlamatLokasi() {
        return AlamatLokasi;
    }
    public void setAlamatLokasi(String AlamatLokasi) {
        this.AlamatLokasi = AlamatLokasi;
    }

    public String getNohpLokasi() {
        return NohpLokasi;
    }
    public void setNohpLokasi(String NohpLokasi) {
        this.NohpLokasi = NohpLokasi;
    }

    public String getLinkLokasi() {return LinkLokasi;}
    public void setLinkLokasi(String LinkLokasi) {
        this.LinkLokasi = LinkLokasi;
    }

    public lokasi(String NamaLokasi, String AlamatLokasi, String NohpLokasi, String LinkLokasi) {
        this.NamaLokasi = NamaLokasi;
        this.AlamatLokasi = AlamatLokasi;
        this.NohpLokasi = NohpLokasi;
        this.LinkLokasi = LinkLokasi;
    }
    public lokasi(){

    }
}
