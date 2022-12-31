package crud.si20.pendataantoko;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;

    CardView cvProduk, cvKaryawan, cvPenjualan, cvToko, cvPemilik;
    ImageView btnLogout;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.cvPenjualan).setOnClickListener(this);
        findViewById(R.id.cvProduk).setOnClickListener(this);
        findViewById(R.id.cvKaryawan).setOnClickListener(this);
        findViewById(R.id.cvToko).setOnClickListener(this);
        findViewById(R.id.cvPemilik).setOnClickListener(this);
        findViewById(R.id.cvLokasi).setOnClickListener(this);
        findViewById(R.id.btnLogout).setOnClickListener(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
//                Toast.makeText(MainActivity.this, "ad initializated", Toast.LENGTH_SHORT).show();
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Toast.makeText(MainActivity.this, adError.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
//                Toast.makeText(MainActivity.this, "ad loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });


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
            case R.id.cvPemilik:
                startActivity(new Intent(MainActivity.this, PemilikActivity.class));
                break;
            case R.id.cvLokasi:
                startActivity(new Intent(MainActivity.this, LokasiActivity.class));
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