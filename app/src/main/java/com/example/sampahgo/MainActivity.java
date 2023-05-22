package com.example.sampahgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser userSaatini;

    private DrawerLayout drawerLayout;
    private ImageView ivMenu;
    private TextView akun;
    private RecyclerView rvMenu;
    static ArrayList<String> arrayList = new ArrayList<>();
    static ArrayList<Integer> image = new ArrayList<>();
    private MainDrawerAdapter adapter;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extra = getIntent().getExtras();
        if(extra != null){
            userId = extra.getString("userId");
        }


        CardView history = findViewById(R.id.History);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open = new Intent(MainActivity.this, History.class);
                open.putExtra("userId", userId);
                startActivity(open);
            }
        });

        CardView penjemputan = findViewById(R.id.Penjemputan);
        penjemputan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open = new Intent(MainActivity.this, Penjemputan.class);
                open.putExtra("userId", userId);
                startActivity(open);
            }
        });

        CardView kategori = findViewById(R.id.Kategori);
        kategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open = new Intent(MainActivity.this, Kategori.class);
                startActivity(open);
            }
        });
        CardView lokasi = findViewById(R.id.Lokasi);
        lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open = new Intent(MainActivity.this, LokasiActivity.class);
                startActivity(open);
            }
        });
        CardView penukaran = findViewById(R.id.Penukaran);
        penukaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open = new Intent(MainActivity.this, Penukaran.class);
                open.putExtra("userId", userId);
                startActivity(open);
            }
        });

        akun = findViewById(R.id.akunPengguna);
        akun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent keAkun = new Intent(MainActivity.this, AkunPengguna.class);
                keAkun.putExtra("userId", userId);
                startActivity(keAkun);
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        ivMenu = findViewById(R.id.ivMenu);
        rvMenu = findViewById(R.id.rvMenu);

        arrayList.clear();

        arrayList.add("BANTUAN");
        arrayList.add("TENTANG");
        arrayList.add("KELUAR");

        image.add(R.drawable.baseline_help_24);
        image.add(R.drawable.baseline_info_24);
        image.add(R.drawable.baseline_logout_24);

        adapter = new MainDrawerAdapter(this, arrayList, image);

        rvMenu.setLayoutManager(new LinearLayoutManager(this));
        rvMenu.setAdapter(adapter);

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

}