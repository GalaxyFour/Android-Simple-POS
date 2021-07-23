package project.erwin.pos;

public class ModelProduk {
    int kode_produk;
    String nama_produk;
    long harga_pokok;
    long harga_jual;
    int stok;

    public int getKode_produk() {
        return kode_produk;
    }

    public void setKode_produk(int kode_produk) {
        this.kode_produk = kode_produk;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public long getHarga_pokok() {
        return harga_pokok;
    }

    public void setHarga_pokok(long harga_pokok) {
        this.harga_pokok = harga_pokok;
    }

    public long getHarga_jual() {
        return harga_jual;
    }

    public void setHarga_jual(long harga_jual) {
        this.harga_jual = harga_jual;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }
}
