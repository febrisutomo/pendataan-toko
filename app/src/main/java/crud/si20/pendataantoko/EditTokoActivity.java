package crud.si20.pendataantoko;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class EditTokoActivity extends AppCompatActivity {

    EditText etNama, etAlamat, etNoHp, etPemilik;
    Button btnKembali, btnSimpan;

    String nama, alamat, noHp, pemilik;

    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_toko);

        etNama = findViewById(R.id.etNama);
        etAlamat = findViewById(R.id.etAlamat);
        etNoHp = findViewById(R.id.etNoHp);
        etPemilik = findViewById(R.id.etPemilik);

        btnSimpan = findViewById(R.id.btnSimpan);
        btnKembali = findViewById(R.id.btnKembali);

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

        getData();
    }

    private void updateData() {
        nama = etNama.getText().toString();
        alamat = etAlamat.getText().toString();
        pemilik = etPemilik.getText().toString();
        noHp = etNoHp.getText().toString();


        if (nama.isEmpty()) {
            etNama.setError("Nama tidak boleh kosong!");
            etNama.requestFocus();
        } else if (alamat.isEmpty()) {
            etAlamat.setError("Alamat tidak boleh kosong!");
            etAlamat.requestFocus();
        } else if (pemilik.isEmpty()) {
            etPemilik.setError("Pemilik tidak boleh kosong!");
            etPemilik.requestFocus();
        } else if (noHp.isEmpty()) {
            etNoHp.setError("No. HP tidak boleh kosong!");
            etNoHp.requestFocus();
        } else {

            String getKey = getIntent().getExtras().getString("key");


            db.child("Toko").child(getKey)
                    .setValue(new Toko(nama, alamat, pemilik, noHp))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EditTokoActivity.this, "Data berhasil diupdate!", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditTokoActivity.this, "Data gagal diupdate!", Toast.LENGTH_SHORT).show();
                        }
                    });

        }


    }

    private void getData() {
        String getNama = getIntent().getExtras().getString("nama");
        String getAlamat = getIntent().getExtras().getString("alamat");
        String getNoHp = getIntent().getExtras().getString("noHp");
        String getPemilik = getIntent().getExtras().getString("pemilik");


        etNama.setText(getNama);
        etAlamat.setText(getAlamat);
        etNoHp.setText(getNoHp);
        etPemilik.setText(getPemilik);

    }


}