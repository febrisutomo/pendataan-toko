package crud.si20.pendataantoko;

public class Penjualan {

    String tanggal, produk, harga, jumlah, key;

    public Penjualan(){

    }

    public Penjualan(String tanggal, String produk, String harga, String jumlah){
        this.tanggal = tanggal;
        this.produk = produk;
        this.harga = harga;
        this.jumlah = jumlah;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setProduk(String produk) {
        this.produk = produk;
    }

    public String getProduk() {
        return produk;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getHarga() {
        return harga;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
