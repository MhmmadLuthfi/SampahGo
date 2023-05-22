package util;

public class user {

    private String username;
    private String userID;
    private String alamat;
    private String notelp;
    private String pertama;
    private String terakhir;
    private String email;
    private String foto;
    private String poin;

    public String getPoin() {
        return poin;
    }

    public void setPoin(String poin) {
        this.poin = poin;
    }

    private static user instance;

    public static user getInstance(){
        if (instance == null){
            instance = new user();
        }
        return instance;
    }
    public user(){

    }
    public String getUsername(){
        return username;
    }

    public String getUserID(){
        return userID;
    }
    public String getAlamat(){
        return alamat;
    }
    public String getNotelp(){
        return notelp;
    }

    public String getPertama() {
        return pertama;
    }

    public void setPertama(String pertama) {
        this.pertama = pertama;
    }

    public String getTerakhir() {
        return terakhir;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTerakhir(String terakhir) {
        this.terakhir = terakhir;
    }

    //Setter
    public void setUsername(String username){
        this.username = username;
    }

    public void setUserID(String userID){
        this.userID = userID;
    }
    public void setAlamat(String alamat){
        this.alamat = alamat;
    }
    public void setNotelp(String notelp) { this.notelp = notelp; }
}
