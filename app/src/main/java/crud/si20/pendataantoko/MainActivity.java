package crud.si20.pendataantoko;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;

    CardView cvProduk, cvKaryawan, cvPenjualan, cvToko;
    ImageView btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();

        cvPenjualan = findViewById(R.id.cvPenjualan);
        cvProduk = findViewById(R.id.cvProduk);
        cvKaryawan = findViewById(R.id.cvKaryawan);
        cvToko = findViewById(R.id.cvToko);
        btnLogout = findViewById(R.id.btnLogout);

        cvPenjualan.setOnClickListener(this);
        cvProduk.setOnClickListener(this);
        cvKaryawan.setOnClickListener(this);
        cvToko.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvPenjualan:
                startActivity(new Intent(MainActivity.this, PenjualanActivity.class));
                break;
            case R.id.cvProduk:
                startActivity(new Intent(MainActivity.this, ProdukActivity.class));
                break;
            case R.id.cvKaryawan:
                startActivity(new Intent(MainActivity.this, KaryawanActivity.class));
                break;
            case R.id.cvToko:
                startActivity(new Intent(MainActivity.this, TokoActivity.class));
                break;
            case R.id.btnLogout:
                mAuth.signOut();
                Toast.makeText(MainActivity.this, "Anda berhasil logout!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;

            default:
                break;
        }
    }
}