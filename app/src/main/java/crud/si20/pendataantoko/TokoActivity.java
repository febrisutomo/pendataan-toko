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


public class TokoActivity extends AppCompatActivity {

    TokoViewAdapter adapter;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    ArrayList<Toko> tokoList;
    ArrayList<Pemilik> pemilikList;
    RecyclerView recyclerView;
    EditText etSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toko);

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


        recyclerView = findViewById(R.id.rvToko);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        pemilikList = new ArrayList<>();

        getPemilik();

        showData();


    }

    private void getPemilik() {

        db.child("Pemilik").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pemilikList = new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()){
                    Pemilik pemilik= item.getValue(Pemilik.class);

                    pemilik.setKey(item.getKey());
                    pemilikList.add(pemilik);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showData() {
        db.child("Toko").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tokoList = new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()){
                    Toko toko= item.getValue(Toko.class);

                    toko.setKey(item.getKey());
                    tokoList.add(toko);
                }

                adapter = new TokoViewAdapter(tokoList, pemilikList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void filter(String text) {
        ArrayList<Toko> filteredList = new ArrayList<>();

        for (Toko item : tokoList){
            if (item.getNama().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);
    }
}