package crud.si20.pendataantoko;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class EditPenjualanActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText etTanggal, etJumlah;
    Spinner spinPelanggan, spinProduk;

    TextView tvHarga, tvBayar;

    Button btnSimpan, btnKembali, btnCustomer;

    DatabaseReference db = FirebaseDatabase.getInstance().getReference();


    ArrayList<Produk> produkList;

    ArrayList<String> customerNameList, produkNameList;

    String tanggal, jumlah, produk;

    int harga, bayar;

    ArrayAdapter<String> adapterProduk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_penjualan);

        etTanggal = findViewById(R.id.etTanggal);
        spinProduk = findViewById(R.id.spinProduk);
        tvHarga = findViewById(R.id.tvHarga);
        etJumlah = findViewById(R.id.etJumlah);
        tvBayar = findViewById(R.id.tvBayar);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnKembali = findViewById(R.id.btnKembali);

        customerNameList = new ArrayList<>();

        produkList = new ArrayList<>();
        produkNameList = new ArrayList<>();
        
        produkNameList.add("Pilih Produk");
        
        adapterProduk = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, produkNameList);
        spinProduk.setAdapter(adapterProduk);
        
        getProduk();

        getData();

        etTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etTanggal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String getTanggal = etTanggal.getText().toString();
                        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(getTanggal);
                        datePickerFragment.show(getSupportFragmentManager(), "date_picker");
                    }
                });
            }
        });

        spinProduk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    harga = Integer.parseInt(produkList.get(position - 1).getHarga());
                    String hargaF = String.format(Locale.US, "%,d", harga).replace(",", ".");
                    tvHarga.setText(hargaF);
                } else {
                    harga = 0;
                    tvHarga.setText("0");
                }

                int jumlah = Integer.parseInt(etJumlah.getText().toString());
                bayar = harga * jumlah;
                String bayarF = String.format(Locale.US, "%,d", bayar).replace(",", ".");
                tvBayar.setText(bayarF);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        etJumlah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {


                if (s.length() > 0) {
                    int jumlah = Integer.parseInt(s.toString());
                    bayar = harga * jumlah;
                    String bayarF = String.format(Locale.US, "%,d", bayar).replace(",", ".");
                    tvBayar.setText(bayarF);
                } else {
                    tvBayar.setText("0");
                }
            }
        });


        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });


        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getData() {
        String getTanggal = getIntent().getExtras().getString("tanggal");

        String getHarga = getIntent().getExtras().getString("harga");
        String getJumlah = getIntent().getExtras().getString("jumlah");

        harga = Integer.parseInt(getHarga);

        etTanggal.setText(getTanggal);
        etJumlah.setText(getJumlah);

        String hargaF = String.format(Locale.US, "%,d", Integer.parseInt(getHarga)).replace(",", ".");
        tvHarga.setText(hargaF);

        int getBayar = Integer.parseInt(getHarga) * Integer.parseInt(getJumlah);
        String bayarF = String.format(Locale.US, "%,d", getBayar).replace(",", ".");
        tvBayar.setText(bayarF);

    }

    private void updateData() {
        tanggal = etTanggal.getText().toString();
        produk = spinProduk.getSelectedItem().toString();
        jumlah = etJumlah.getText().toString();

        if (tanggal.isEmpty()) {
            etTanggal.setError("Tanggal tidak boleh kosong!");
            etTanggal.requestFocus();
        } else if (jumlah.equals("0") || jumlah.equals("")) {
            etJumlah.setError("Jumlah tidak boleh kosong!");
            etJumlah.requestFocus();
        } else {

            String getKey = getIntent().getExtras().getString("key");

            db.child("Penjualan").child(getKey)
                    .setValue(new Penjualan(tanggal, produk, Integer.toString(harga), jumlah))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Data berhasil diupdate!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });


        }

    }

    private void getProduk() {
        db.child("Produk").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                produkList.clear();
                adapterProduk.clear();

                for (DataSnapshot item : snapshot.getChildren()) {
                    Produk produk = item.getValue(Produk.class);

                    produk.setKey(item.getKey());
                    produkList.add(produk);
                }

                produkNameList.add("Pilih Produk");
                for (Produk produk : produkList) {
                    produkNameList.add(produk.getNama());
                }
                String getProduk = getIntent().getExtras().getString("produk");
                ArrayAdapter<String> produkAdp = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, produkNameList);
                spinProduk.setAdapter(produkAdp);
                spinProduk.setSelection(produkAdp.getPosition(getProduk.trim()));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String date =  new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
        etTanggal.setText(date);
    }
}