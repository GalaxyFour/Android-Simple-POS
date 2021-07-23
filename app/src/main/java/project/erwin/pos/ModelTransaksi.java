package project.erwin.pos;

import java.io.Serializable;

public class ModelTransaksi implements Serializable {
    String namaProduk;
    int kodeProduk;
    int jumlahItem;
    long hargaJual;

    public long getHargaJual() {
        return hargaJual;
    }

    public void setHargaJual(long hargaJual) {
        this.hargaJual = hargaJual;
    }

    public int getKodeProduk() {
        return kodeProduk;
    }

    public void setKodeProduk(int kodeProduk) {
        this.kodeProduk = kodeProduk;
    }

    public int getJumlahItem() {
        return jumlahItem;
    }

    public void setJumlahItem(int jumlahItem) {
        this.jumlahItem = jumlahItem;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

}
