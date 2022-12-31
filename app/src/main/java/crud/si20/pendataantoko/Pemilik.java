package crud.si20.pendataantoko;

public class Pemilik {

    String nama, noHp, jk, gambar, toko, key;

    public Pemilik(){

    }

    public Pemilik(String nama, String jk, String noHp, String gambar, String toko ){
        this.nama = nama;
        this.noHp = noHp;
        this.jk = jk;
        this.gambar = gambar;
        this.toko = toko;
    }


    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getJk() {
        return jk;
    }

    public void setJk(String jk) {
        this.jk = jk;
    }

    public String getToko() {
        return toko;
    }

    public void setToko(String toko) {
        this.toko = toko;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}