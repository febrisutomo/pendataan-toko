package crud.si20.pendataantoko;

import static android.text.TextUtils.isEmpty;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProdukViewAdapter extends RecyclerView.Adapter<ProdukViewAdapter.ProdukViewHolder> {

    private ArrayList<Produk> myList;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    public ProdukViewAdapter(ArrayList<Produk> myList) {
        this.myList = myList;
    }

    public void filterList(ArrayList<Produk> filteredList) {
        myList = new ArrayList<>();
        myList.addAll(filteredList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProdukViewAdapter.ProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.design_produk, parent, false);
        return new ProdukViewAdapter.ProdukViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdukViewAdapter.ProdukViewHolder holder, int position) {
        String nama = myList.get(position).getNama();
        String berat = myList.get(position).getBerat();
        String tglExp = myList.get(position).getTglexp();
        String harga = myList.get(position).getHarga();
        String gambar = myList.get(position).getGambar();
        String key = myList.get(position).getKey();


        holder.tvNama.setText(": " + nama);
        holder.tvBerat.setText(": " + berat);
        holder.tvHarga.setText(": Rp " + harga);
        holder.tvTglExp.setText(": " + tglExp);

        if (isEmpty(gambar)) {
            holder.ivGambar.setImageResource(R.drawable.image);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(gambar.trim()).placeholder(R.drawable.image)
                    .into(holder.ivGambar);
        }

        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final String[] action = {"Edit", "Hapus"};
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        switch (i) {
                            case 0:
                                Bundle bundle = new Bundle();
                                bundle.putString("nama", nama);
                                bundle.putString("harga", harga);
                                bundle.putString("tglExp", tglExp);
                                bundle.putString("berat", berat);
                                bundle.putString("gambar", gambar);
                                bundle.putString("key", key);

                                Intent intent = new Intent(view.getContext(), EditProdukActivity.class);
                                intent.putExtras(bundle);
                                view.getContext().startActivity(intent);
                                break;
                            case 1:
                                delete(myList.get(holder.getAdapterPosition()), holder.getAdapterPosition(), view.getContext());

                                break;
                        }

                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class ProdukViewHolder extends RecyclerView.ViewHolder {

        TextView tvNama, tvHarga, tvBerat, tvTglExp;
        ImageView ivGambar;
        CardView card;

        public ProdukViewHolder(@NonNull View view) {
            super(view);

            tvNama = view.findViewById(R.id.tvNama);
            tvHarga = view.findViewById(R.id.tvHarga);
            tvBerat= view.findViewById(R.id.tvBerat);
            tvTglExp = view.findViewById(R.id.tvTglExp);
            ivGambar = view.findViewById(R.id.ivGambar);
            card = view.findViewById(R.id.card);

        }
    }

    public void delete(Produk product, int position, Context context) {
        if (db != null) {
            db.child("Produk").child(product.getKey())
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Data berhasil dihapus!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
