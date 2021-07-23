package project.erwin.pos;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterProduk2 extends RecyclerView.Adapter<AdapterProduk2.ViewHolder> {
    private ArrayList<ModelProduk> produk;
    private Context context;

    public AdapterProduk2(Context context, ArrayList<ModelProduk> produks) {
        this.produk = produks;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //mengatur content_list_produk_editor.xmltor.xml sebagai template
        // item dari list produk yang akan ditampilkan
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_list_laba, null);
        return new AdapterProduk2.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        //untuk menampilkan data produk pada content_list_produk_editor_editor.xml
        holder.tvcodeTrans.setText(produk.get(position).getNama_produk() + "");
        holder.tvLaba.setText(produk.get(position).getStok() + "");
    }

    @Override
    public int getItemCount() {
        return produk.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvcodeTrans, tvLaba;
        ViewHolder(View itemView){
            super(itemView);
            tvcodeTrans = itemView.findViewById(R.id.tvcodeTrans);
            tvLaba = itemView.findViewById(R.id.tvLaba);
        }
    }
}
