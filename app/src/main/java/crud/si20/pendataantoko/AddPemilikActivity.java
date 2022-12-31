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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class AddPemilikActivity extends AppCompatActivity {
    EditText etNama, etAlamatToko, etNamaToko, etNoHp;
    Button btnKembali, btnSimpan, btnGambar;
    RadioGroup rgJk;
    RadioButton rbMale, rbFemale;
    ImageView ivGambar;

    String nama, namaToko, alamatToko, noHp, jk;

    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    Uri uri;

    StorageReference storage = FirebaseStorage.getInstance().getReference();

    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pemilik);

        etNama = findViewById(R.id.etNama);
        etAlamatToko = findViewById(R.id.etAlamatToko);
        etNamaToko = findViewById(R.id.etNamaToko);
        etNoHp = findViewById(R.id.etNoHp);
        rgJk = findViewById(R.id.rgJk);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);

        btnSimpan = findViewById(R.id.btnSimpan);
        btnKembali = findViewById(R.id.btnKembali);

        btnGambar = findViewById(R.id.btnGambar);

        ivGambar = findViewById(R.id.ivGambar);


        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setCancelable(false);

        btnGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFoto();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeData();
            }
        });

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void storeData() {
        nama = etNama.getText().toString();
        namaToko = etNamaToko.getText().toString();
        alamatToko = etAlamatToko.getText().toString();
        noHp = etNoHp.getText().toString();

        int checkedId = rgJk.getCheckedRadioButtonId();
        RadioButton checkedJk = findViewById(checkedId);

        jk = checkedJk.getText().toString();

        if (nama.isEmpty()) {
            etNama.setError("Nama tidak boleh kosong!");
            etNama.requestFocus();
        } else if (noHp.isEmpty()) {
            etNoHp.setError("No. HP tidak boleh kosong!");
            etNoHp.requestFocus();
        } else if (alamatToko.isEmpty()) {
            etAlamatToko.setError("Alamat Toko tidak boleh kosong!");
            etAlamatToko.requestFocus();
        } else if (namaToko.isEmpty()) {
            etNamaToko.setError("Nama Toko tidak boleh kosong!");
            etNamaToko.requestFocus();
        } else {

            Bitmap bt = ((BitmapDrawable) ivGambar.getDrawable()).getBitmap();

            ByteArrayOutputStream st = new ByteArrayOutputStream();

            bt.compress(Bitmap.CompressFormat.JPEG, 100, st);
            byte[] bytes = st.toByteArray();
            String namaGambar = UUID.randomUUID() + ".jpg";

            final String path = "gambar/" + namaGambar;

            progress.show();
            UploadTask up = storage.child(path).putBytes(bytes);

            up.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    db.child("Toko").push()
                                            .setValue(new Toko(namaToko, alamatToko), new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                                                    db.child("Pemilik").push()
                                                            .setValue(new Pemilik(nama, jk, noHp, uri.toString().trim(), ref.getKey()))
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    progress.dismiss();
                                                                    Toast.makeText(AddPemilikActivity.this, "Data berhasil ditambahkan!", Toast.LENGTH_LONG).show();
                                                                    finish();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(AddPemilikActivity.this, "Data gagal ditambahkan!", Toast.LENGTH_LONG).show();
                                                                    progress.dismiss();
                                                                }
                                                            });
                                                }
                                            });


                                }
                            });
                }
            });
        }
    }


    private void getFoto() {
        Intent ImgIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(ImgIntent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    ivGambar.setVisibility(View.VISIBLE);
                    Bitmap bt = (Bitmap) data.getExtras().get("data");
                    ivGambar.setImageBitmap(bt);
                }
                break;
            case REQUEST_CODE_GALLERY:
                if (resultCode == RESULT_OK) {
                    ivGambar.setVisibility(View.VISIBLE);
                    uri = data.getData();
                    ivGambar.setImageURI(uri);
                }
        }
    }


}