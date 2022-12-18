package crud.si20.pendataantoko;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class EditProdukActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText etNama, etHarga, etTglExp;
    Button btnKembali, btnSimpan, btnGambar;
    Spinner spinBerat;
    ImageView ivGambar;
    String nama, harga, berat, tglExp;

    Uri uri;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    StorageReference storage = FirebaseStorage.getInstance().getReference();

    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_produk);

        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setCancelable(false);

        etNama = findViewById(R.id.etNama);
        etHarga = findViewById(R.id.etHarga);
        etTglExp = findViewById(R.id.etTglExp);
        spinBerat = findViewById(R.id.spinBerat);

        btnSimpan = findViewById(R.id.btnSimpan);
        btnKembali = findViewById(R.id.btnKembali);
        btnGambar = findViewById(R.id.btnGambar);

        ivGambar = findViewById(R.id.ivGambar);

        etTglExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getTanggal = etTglExp.getText().toString();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(getTanggal);
                datePickerFragment.show(getSupportFragmentManager(), "date_picker");
            }
        });

        btnGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFoto();
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

        getData();
    }

    private void updateData() {
        nama = etNama.getText().toString();
        harga = etHarga.getText().toString();
        tglExp = etTglExp.getText().toString();
        berat = spinBerat.getSelectedItem().toString();

        if (nama.isEmpty()) {
            etNama.setError("Nama tidak boleh kosong!");
            etNama.requestFocus();
        } else if (harga.isEmpty()) {
            etHarga.setError("Harga tidak boleh kosong!");
            etHarga.requestFocus();
        } else if (tglExp.isEmpty()) {
            etTglExp.setError("Tgl Exp. tidak boleh kosong!");
            etTglExp.requestFocus();
        } else {
            ivGambar.setDrawingCacheEnabled(true);
            ivGambar.buildDrawingCache();

            Bitmap bitmap = ((BitmapDrawable) ivGambar.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bytes = stream.toByteArray();

            String fileName = UUID.randomUUID() + ".jpg";

            String pathImage = "gambar/" + fileName;

            String getKey = getIntent().getExtras().getString("key");

            progress.show();

            UploadTask uploadTask = storage.child(pathImage).putBytes(bytes);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    db.child("Produk").child(getKey)
                                            .setValue(new Produk(nama, berat, harga, tglExp, uri.toString().trim()))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    progress.dismiss();
                                                    Toast.makeText(EditProdukActivity.this, "Data berhasil diupdate!", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }

                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progress.dismiss();
                                                    Toast.makeText(EditProdukActivity.this, "Data gagal diupdate!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                }
            });
        }
    }

    private void getData() {
        String getNama = getIntent().getExtras().getString("nama");
        String getBerat = getIntent().getExtras().getString("berat");
        String getHarga = getIntent().getExtras().getString("harga");
        String getTglExp = getIntent().getExtras().getString("tglExp");

        String getGambar = getIntent().getExtras().getString("gambar");


        if (getGambar.isEmpty()) {
            ivGambar.setImageResource(R.drawable.image);
        } else {
            Glide.with(EditProdukActivity.this)
                    .load(getGambar)
                    .into(ivGambar);
        }

        etNama.setText(getNama);
        etHarga.setText(getHarga);
        etTglExp.setText(getTglExp);

        String[] strBerat = getResources().getStringArray(R.array.berat);
        ArrayAdapter<String> beratAdp = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, strBerat);
        spinBerat.setAdapter(beratAdp);
        spinBerat.setSelection(beratAdp.getPosition(getBerat.trim()));

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String date = new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
        etTglExp.setText(date);
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