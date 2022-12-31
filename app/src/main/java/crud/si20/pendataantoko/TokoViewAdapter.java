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

public class TokoViewAdapter extends RecyclerView.Adapter<TokoViewAdapter.TokoViewHolder> {

    private ArrayList<Toko> tokoList;
    private ArrayList<Pemilik> pemilikList;

    String nama, key, namaPemilik;

    public TokoViewAdapter(ArrayList<Toko> tokoList, @NonNull ArrayList<Pemilik> pemilikList) {
        this.tokoList = tokoList;
        this.pemilikList = pemilikList;
    }

    public void filterList(ArrayList<Toko> filteredList) {
        tokoList = new ArrayList<>();
        tokoList.addAll(filteredList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TokoViewAdapter.TokoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.design_toko, parent, false);
        return new TokoViewAdapter.TokoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TokoViewAdapter.TokoViewHolder holder, int position) {
        nama = tokoList.get(position).getNama();
        key = tokoList.get(position).getKey();
        namaPemilik = "";

        for (Pemilik pemilik : pemilikList) {
            if (pemilik.getToko().equals(key)) {
                namaPemilik = pemilik.getNama();
            }
        }

        holder.tvNama.setText(": " + nama);
        holder.tvPemilik.setText(": " + namaPemilik);


    }

    @Override
    public int getItemCount() {
        return tokoList.size();
    }

    public class TokoViewHolder extends RecyclerView.ViewHolder {

        TextView tvNama, tvPemilik;
        CardView card;

        public TokoViewHolder(@NonNull View view) {
            super(view);

            tvNama = view.findViewById(R.id.tvNama);
            tvPemilik = view.findViewById(R.id.tvPemilik);
            card = view.findViewById(R.id.card);

        }
    }


}