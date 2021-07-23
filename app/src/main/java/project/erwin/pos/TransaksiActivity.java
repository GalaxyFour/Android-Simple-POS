package project.erwin.pos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class TransaksiActivity extends AppCompatActivity {
    private RecyclerView listTrans;
    public Button btnBayar, btnSelesai;
    public EditText etTotalBayar;
    public TextView tvKembalian;

    private DBHelper db;
    public ArrayList<ModelTransaksi> transaksiModel, transaksiModelLama;
    public long totalHarga;
    public long totalKeuntungan;
    public String kode_transaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);
        db = new DBHelper(this);
        transaksiModel = new ArrayList<>();
        transaksiModel.clear();
        transaksiModelLama = (ArrayList<ModelTransaksi>) getIntent().getSerializableExtra("transaksiModels");
        listTrans = findViewById(R.id.listTransaksi);

        btnBayar = findViewById(R.id.btnBayar);
        etTotalBayar = findViewById(R.id.etTotalBayar);

        listTrans.setLayoutManager(new LinearLayoutManager(this));

        String query = "SELECT * FROM keranjang WHERE jumlah_produk > 0";
        //mengeksekusi custom query
        Cursor cursor = db.customQuery(query);
        //mengarahkan cursor pada baris pertama hasil query
        cursor.moveToFirst();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ModelTransaksi model = new ModelTransaksi();
                model.setKodeProduk(cursor.getInt(cursor.getColumnIndex("kode_produk")));
                model.setNamaProduk(cursor.getString(cursor.getColumnIndex("nama_produk")));
                model.setHargaJual(cursor.getLong(cursor.getColumnIndex("harga_jual")));
                model.setJumlahItem(cursor.getInt(cursor.getColumnIndex("jumlah_produk")));
                transaksiModel.add(model);

                long harga = cursor.getLong(cursor.getColumnIndex("harga_jual"));

                int qty = cursor.getInt(cursor.getColumnIndex("jumlah_produk"));

                totalHarga = totalHarga + (harga * qty);

                //hitung keuntungan per transaksi
                long modal = cursor.getLong(cursor.getColumnIndex("harga_pokok"));
                long hargaJual = cursor.getLong(cursor.getColumnIndex("harga_jual"));
                long keuntungan = (hargaJual - modal) * qty;
                totalKeuntungan = totalKeuntungan + keuntungan;
            } while (cursor.moveToNext());
            btnBayar.setText("Bayar : Rp " + totalHarga);

            AdapterTransaksi adapterTransaksi = new AdapterTransaksi(this, transaksiModel);
            listTrans.setAdapter(adapterTransaksi);
            listTrans.getAdapter().notifyDataSetChanged();
        }

        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalHarga != 0) {
                    if (etTotalBayar.getText().toString().isEmpty()) {
                        Toast.makeText(TransaksiActivity.this, "Masukkan Uang Pembeli", Toast.LENGTH_SHORT).show();
                    } else {
                        long uangPembeli = Long.valueOf(etTotalBayar.getText().toString());
                        long kembalian = uangPembeli - totalHarga;

                        if (kembalian < 0) {
                            Snackbar snackbar = Snackbar.make(view, "Uang Tidak Cukup", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        } else {
                            //generate kode transaksi
                            kode_transaksi = TransaksiActivity.getAlphaNumericString(6);
                            db.sdb = db.getWritableDatabase();
                            db.sdb.beginTransaction();
                            try {
                                db.insertKeuntungan(kode_transaksi, totalKeuntungan);
                                db.insertTransaksi(kode_transaksi, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), totalHarga, uangPembeli, kembalian);
                                for (ModelTransaksi detail : transaksiModel) {
                                    db.insertDetailTransaksi(kode_transaksi, detail.kodeProduk, detail.jumlahItem, detail.hargaJual);
                                }
                                db.endTransactionProcess(kode_transaksi);
                                db.sdb.setTransactionSuccessful();
                            } catch (Exception ex) {
                                //Error in between database transaction
                                Toast.makeText(TransaksiActivity.this, "ERROR !", Toast.LENGTH_SHORT).show();
                                return;
                            } finally {
                                db.sdb.endTransaction();
                            }

                            Intent intent = new Intent(TransaksiActivity.this, PembayaranActivity.class);
                            intent.putExtra("harga_total", totalHarga);
                            intent.putExtra("uang_pembeli", uangPembeli);
                            intent.putExtra("uang_kembalian", kembalian);
                            intent.putExtra("total_keuntungan", totalKeuntungan);
                            intent.putExtra("kode_transaksi", kode_transaksi);
                            startActivity(intent);
                            clearTabelKeranjang();
                        }
                    }
                }else{
                    Toast.makeText(TransaksiActivity.this, "Keranjang Masih Kosong !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void clearTabelKeranjang(){
        SQLiteDatabase database = db.getWritableDatabase();
        database.delete("keranjang", null, null);
    }
    static String getAlphaNumericString(int n)
    {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

}
