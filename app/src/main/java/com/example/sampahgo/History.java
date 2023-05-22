package com.example.sampahgo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class History extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private TextView akun;
    private ImageView ivMenu;
    private RecyclerView rvMenu;
    static ArrayList<String> arrayList = new ArrayList<>();
    static ArrayList<Integer> image = new ArrayList<>();
    private MainDrawerAdapter adapter;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Bundle extra = getIntent().getExtras();
        if(extra != null){
            userId = extra.getString("userId");
        }

        CardView home = findViewById(R.id.Home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        akun = findViewById(R.id.akunPengguna1);
        akun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent keAkun = new Intent(History.this, AkunPengguna.class);
                keAkun.putExtra("userId", userId);
                startActivity(keAkun);
            }
        });

        Button penjemputan = findViewById(R.id.jemput);
        penjemputan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open = new Intent(History.this, HistoryPenjemputan.class);
                open.putExtra("userId", userId);
                startActivity(open);
            }
        });

        Button penukaran = findViewById(R.id.tukar);
        penukaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open = new Intent(History.this, HistoryPenukaran.class);
                startActivity(open);
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout1);
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