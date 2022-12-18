package crud.si20.pendataantoko;

public class Toko {
    String nama, alamat, pemilik, noHp, key;

    public Toko(){

    }

    public  Toko(String nama, String alamat, String pemilik, String noHp){
        this.nama = nama;
        this.alamat = alamat;
        this.pemilik = pemilik;
        this.noHp = noHp;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setPemilik(String pemilik) {
        this.pemilik = pemilik;
    }

    public String getPemilik() {
        return pemilik;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
