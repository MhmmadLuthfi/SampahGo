package com.example.sampahgo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import model.tukar;
import ui.PenukaranRecylerAdapter;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HistoryPenukaran extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private List<tukar> tukarList;
    private RecyclerView recyclerView;
    private PenukaranRecylerAdapter penukaranRecylerAdapter;
    private TextView noPostEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_penukaran);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser().getUid();

        noPostEntry = findViewById(R.id.PenukaranListNoPost);
        recyclerView = findViewById(R.id.HasilPenukaranrecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tukarList = new ArrayList<>();
    }
    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        db.collection("tukar").whereEqualTo("penggunaID", user).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    for(QueryDocumentSnapshot tukars : queryDocumentSnapshots){
                        tukar tukar = tukars.toObject(tukar.class);
                        tukarList.add(tukar);
                    }
                    penukaranRecylerAdapter = new PenukaranRecylerAdapter(HistoryPenukaran.this, tukarList);
                    recyclerView.setAdapter(penukaranRecylerAdapter);
                    penukaranRecylerAdapter.notifyDataSetChanged();
                }else{
                    noPostEntry.setVisibility(View.VISIBLE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Keasalahan Apapun
                Toast.makeText(HistoryPenukaran.this, "Ups! Ada yang Salah!" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
}