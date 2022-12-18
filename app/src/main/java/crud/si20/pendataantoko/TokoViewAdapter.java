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

    private ArrayList<Toko> myList;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    public TokoViewAdapter(ArrayList<Toko> myList) {
        this.myList = myList;
    }

    public void filterList(ArrayList<Toko> filteredList) {
        myList = new ArrayList<>();
        myList.addAll(filteredList);
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
        String nama = myList.get(position).getNama();
        String alamat = myList.get(position).getAlamat();
        String pemilik = myList.get(position).getPemilik();
        String noHp = myList.get(position).getNoHp();
        String key = myList.get(position).getKey();


        holder.tvNama.setText(": " + nama);
        holder.tvAlamat.setText(": " + alamat);
        holder.tvPemilik.setText(": " + pemilik);
        holder.tvNoHp.setText(": " + noHp);



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
                                bundle.putString("alamat", alamat);
                                bundle.putString("pemilik", pemilik);
                                bundle.putString("noHp", noHp);
                                bundle.putString("key", key);

                                Intent intent = new Intent(view.getContext(), EditTokoActivity.class);
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

    public class TokoViewHolder extends RecyclerView.ViewHolder {

        TextView tvNama, tvAlamat, tvPemilik, tvNoHp;
        CardView card;

        public TokoViewHolder(@NonNull View view) {
            super(view);

            tvNama = view.findViewById(R.id.tvNama);
            tvPemilik = view.findViewById(R.id.tvPemilik);
            tvAlamat = view.findViewById(R.id.tvAlamat);
            tvNoHp = view.findViewById(R.id.tvNoHp);
            card = view.findViewById(R.id.card);

        }
    }

    public void delete(Toko product, int position, Context context) {
        if (db != null) {
            db.child("Toko").child(product.getKey())
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