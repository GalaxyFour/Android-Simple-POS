package project.erwin.pos;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReportPenjualanActivity extends AppCompatActivity {
    private DBHelper db;
    private int nTotalLaba;

    final Calendar myCalendar = Calendar.getInstance();
    private Button btnKembali;
    private RecyclerView listProduk;
    private TextView tvtotalLaba;
    private EditText txtTanggalPenjualan;
    private ArrayList<ModelProduk> produk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_penjualan);
        db = new DBHelper(this);

        inisialisasi();
        load_data();

//        txtTanggalPenjualan.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//            @Override
//            public void afterTextChanged(Editable editable) {
//                load_data();
//            }
//        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        txtTanggalPenjualan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ReportPenjualanActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReportPenjualanActivity.this, MainActivity.class);
                startActivity(i);
//                clearTabelKeranjang();
            }
        });
    }

//    private void clearTabelKeranjang(){
//        SQLiteDatabase database = db.getWritableDatabase();
//        database.delete("keranjang", null, null);
//    }

    private void load_data() {
//        listProduk.removeAllViewsInLayout();
//        listProduk.removeAllViews();
        //mempersiapkan list
        produk = new ArrayList<>();
        //memastikan list agar kosong
        produk.clear();
        //mengatur layoutmanager RecyclerView
        //agar menggunakan LinearLayout sebagai container
        listProduk.setLayoutManager(new LinearLayoutManager(this));

        String tempSql = " WHERE date(t.tanggal) = '" + txtTanggalPenjualan.getText() + "'";

        String query = "SELECT p.nama_produk, sum(dt.qty) as qty " +
                        "FROM produk p " +
                        "INNER JOIN detail_transaksi dt on dt.kode_produk=p.kode_produk " +
                        "INNER JOIN transaksi t on dt.kode_transaksi=t.kode_transaksi " +
                        " " + tempSql +
                        " GROUP BY p.kode_produk ";
        //mengeksekusi custom query
        Cursor cursor = db.customQuery(query);
        //mengarahkan cursor pada baris pertama hasil query
        cursor.moveToFirst();
        if (cursor != null && cursor.moveToFirst()) {
            nTotalLaba = 0;
            do{
                ModelProduk model = new ModelProduk();
                model.setNama_produk(cursor.getString(cursor.getColumnIndex("nama_produk")));
                model.setStok(cursor.getInt(cursor.getColumnIndex("qty")));
                produk.add(model);

                int totLaba = cursor.getInt(cursor.getColumnIndex("qty"));
                nTotalLaba = nTotalLaba + totLaba;
                tvtotalLaba.setText("Total : " + nTotalLaba);
            }while (cursor.moveToNext());
            //mempersiapkan adapter untuk recyclerView
            AdapterProduk2 adapterLaba = new AdapterProduk2(this, produk);
            //mengatur adapter untuk RecyclerView
            listProduk.setAdapter(adapterLaba);
            listProduk.getAdapter().notifyDataSetChanged();
        }else{
            AdapterProduk2 adapterLaba = new AdapterProduk2(this, produk);
            listProduk.setAdapter(adapterLaba);
            listProduk.getAdapter().notifyDataSetChanged();
        }
    }
    private void inisialisasi() {

        listProduk = findViewById(R.id.listKeuntungan);
        tvtotalLaba = findViewById(R.id.tvtotalLaba);
        btnKembali = findViewById(R.id.btnKembali);
        txtTanggalPenjualan = findViewById(R.id.txtTanggalPenjualan);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        txtTanggalPenjualan.setText(dateFormat.format(new Date()));
    }
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txtTanggalPenjualan.setText(sdf.format(myCalendar.getTime()));

        load_data();
    }
}
