package project.erwin.pos;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterTransaksi extends RecyclerView.Adapter<AdapterTransaksi.ViewHolder> {
    private ArrayList<ModelTransaksi> produk;
    private Context context;

    public AdapterTransaksi(Context context, ArrayList<ModelTransaksi> produks) {
        this.produk = produks;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //mengatur content_list_produk_editor.xmltor.xml sebagai template
        // item dari list produk yang akan ditampilkan
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_list_transaksi, null);
        return new AdapterTransaksi.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        //untuk menampilkan data produk pada content_list_produk_editor_editor.xml
        holder.namaProduk.setText(produk.get(position).getNamaProduk() + "");
        holder.jumlahItem.setText(produk.get(position).getJumlahItem() + "");
        long total = produk.get(position).getJumlahItem() * produk.get(position).getHargaJual();
        holder.hargaJual.setText("Rp "+total);
    }

    @Override
    public int getItemCount() {
        return produk.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView namaProduk,jumlahItem,hargaJual;
        ViewHolder(View itemView){
            super(itemView);
            namaProduk = itemView.findViewById(R.id.tvTransNamaProduk);
            jumlahItem = itemView.findViewById(R.id.tvTransJmlItem);
            hargaJual = itemView.findViewById(R.id.tvTransHargaJual);
        }
    }
}