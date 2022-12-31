package crud.si20.pendataantoko;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LokasiViewAdapter extends RecyclerView.Adapter<LokasiViewAdapter.TokoViewHolder> {

    private ArrayList<Toko> tokoList;

    String nama, key, alamat;

    public LokasiViewAdapter(ArrayList<Toko> tokoList) {
        this.tokoList = tokoList;
    }

    public void filterList(ArrayList<Toko> filteredList) {
        tokoList = new ArrayList<>();
        tokoList.addAll(filteredList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LokasiViewAdapter.TokoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.design_lokasi, parent, false);
        return new LokasiViewAdapter.TokoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LokasiViewAdapter.TokoViewHolder holder, int position) {
        nama = tokoList.get(position).getNama();
        alamat = tokoList.get(position).getAlamat();
        key = tokoList.get(position).getKey();

        holder.tvNama.setText(": " + nama);
        holder.tvAlamat.setText(": " + alamat);

    }

    @Override
    public int getItemCount() {
        return tokoList.size();
    }

    public class TokoViewHolder extends RecyclerView.ViewHolder {

        TextView tvNama, tvAlamat;
        CardView card;

        public TokoViewHolder(@NonNull View view) {
            super(view);

            tvNama = view.findViewById(R.id.tvNama);
            tvAlamat = view.findViewById(R.id.tvAlamat);
            card = view.findViewById(R.id.card);

        }
    }


}