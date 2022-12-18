package crud.si20.pendataantoko;

public class Produk {
    public String nama, berat, tglexp, harga, gambar, key;


    public Produk(){

    }

    public Produk(String nama, String berat, String harga, String tglexp, String gambar) {

        this.nama = nama;
        this.berat = berat;
        this.harga = harga;
        this.tglexp = tglexp;
        this.gambar = gambar;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getBerat() {
        return berat;
    }

    public void setBerat(String berat) {
        this.berat = berat;
    }

    public String getTglexp() {
        return tglexp;
    }

    public void setTglexp(String tglexp) {
        this.tglexp = tglexp;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
