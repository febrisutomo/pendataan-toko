package crud.si20.pendataantoko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class PemilikActivity extends AppCompatActivity {

    PemilikViewAdapter adapter;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    ArrayList<Pemilik> pemilikList;
    ArrayList<Toko> tokoList;
    RecyclerView recyclerView;
    EditText etSearch;
    FloatingActionButton btnTambah;

    String namaToko, alamatToko;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemilik);

        etSearch = findViewById(R.id.etSearch);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        btnTambah = findViewById(R.id.btnTambah);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PemilikActivity.this, AddPemilikActivity.class));
            }
        });


        recyclerView = findViewById(R.id.rvPemilik);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        tokoList = new ArrayList<>();
        getToko();

        showData();


    }

    private void getToko() {
        db.child("Toko").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tokoList = new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()){
                    Toko toko= item.getValue(Toko.class);

                    toko.setKey(item.getKey());
                    tokoList.add(toko);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showData() {
        db.child("Pemilik").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pemilikList = new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()){
                    Pemilik pemilik= item.getValue(Pemilik.class);

                    pemilik.setKey(item.getKey());
                    pemilikList.add(pemilik);
                }

                adapter = new PemilikViewAdapter(pemilikList, tokoList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void filter(String text) {
        ArrayList<Pemilik> filteredList = new ArrayList<>();

        for (Pemilik item : pemilikList){
            namaToko = "";
            alamatToko = "";
            for (Toko toko : tokoList) {
                if (toko.getKey().equals(item.getToko())) {
                    namaToko = toko.getNama();
                    alamatToko = toko.getAlamat();
                }
            }
            if (item.getNama().toLowerCase().contains(text.toLowerCase()) || item.getNoHp().toLowerCase().contains(text.toLowerCase())  || item.getJk().toLowerCase().contains(text.toLowerCase()) || namaToko.toLowerCase().contains(text.toLowerCase()) || alamatToko.toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);
    }
}