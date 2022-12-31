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

public class PemilikViewAdapter extends RecyclerView.Adapter<PemilikViewAdapter.PemilikViewHolder> {

    private ArrayList<Pemilik> pemilikList;
    private ArrayList<Toko> tokoList;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    String nama, noHp, jk, gambar, key, tokoKey, namaToko, alamatToko;

    public PemilikViewAdapter(ArrayList<Pemilik> pemilikList, @NonNull ArrayList<Toko> tokoList) {
        this.pemilikList = pemilikList;
        this.tokoList = tokoList;
    }

    public void filterList(ArrayList<Pemilik> filteredList) {
        pemilikList = new ArrayList<>();
        pemilikList.addAll(filteredList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PemilikViewAdapter.PemilikViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.design_pemilik, parent, false);
        return new PemilikViewAdapter.PemilikViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PemilikViewAdapter.PemilikViewHolder holder, int position) {
        nama = pemilikList.get(position).getNama();
        noHp = pemilikList.get(position).getNoHp();
        jk = pemilikList.get(position).getJk();
        gambar = pemilikList.get(position).getGambar();
        key = pemilikList.get(position).getKey();
        tokoKey = pemilikList.get(position).getToko();
        namaToko = "";
        alamatToko = "";

        for (Toko toko : tokoList) {
            if (toko.getKey().equals(tokoKey)) {
                namaToko = toko.getNama();
                alamatToko = toko.getAlamat();
            }
        }

        holder.tvNama.setText(": " + nama);
        holder.tvJk.setText(": " + jk);
        holder.tvNoHp.setText(": " + noHp);

        holder.tvNamaToko.setText(": " + namaToko);
        holder.tvAlamatToko.setText(": " + alamatToko);

        if (isEmpty(gambar)) {
            holder.ivGambar.setImageResource(R.drawable.user);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(gambar.trim()).placeholder(R.drawable.user)
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
                                bundle.putString("jk", jk);
                                bundle.putString("noHp", noHp);
                                bundle.putString("gambar", gambar);
                                bundle.putString("tokoKey", tokoKey);
                                bundle.putString("namaToko", namaToko);
                                bundle.putString("alamatToko", alamatToko);
                                bundle.putString("key", key);

                                Intent intent = new Intent(view.getContext(), EditPemilikActivity.class);
                                intent.putExtras(bundle);
                                view.getContext().startActivity(intent);
                                break;
                            case 1:
                                delete(pemilikList.get(holder.getAdapterPosition()), holder.getAdapterPosition(), view.getContext());

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
        return pemilikList.size();
    }

    public class PemilikViewHolder extends RecyclerView.ViewHolder {

        TextView tvNama, tvAlamat, tvJk, tvNoHp, tvNamaToko, tvAlamatToko;
        ImageView ivGambar;
        CardView card;

        public PemilikViewHolder(@NonNull View view) {
            super(view);

            tvNama = view.findViewById(R.id.tvNama);
            tvJk = view.findViewById(R.id.tvJk);
            tvAlamat = view.findViewById(R.id.tvAlamat);
            tvNoHp = view.findViewById(R.id.tvNoHp);
            ivGambar = view.findViewById(R.id.ivGambar);
            card = view.findViewById(R.id.card);

            tvNamaToko = view.findViewById(R.id.tvNamaToko);
            tvAlamatToko = view.findViewById(R.id.tvAlamatToko);

        }
    }

    public void delete(Pemilik pemilik, int position, Context context) {
        if (db != null) {
            db.child("Pemilik").child(pemilik.getKey())
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            db.child("Toko").child(pemilik.getToko())
                                    .removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Data berhasil dihapus!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
        }
    }
}