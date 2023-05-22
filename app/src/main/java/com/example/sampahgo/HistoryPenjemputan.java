package com.example.sampahgo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import model.jemput;
import ui.PenjemputanRecylerAdapter;
import ui.PenukaranRecylerAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HistoryPenjemputan extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private String user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private StorageReference storageReference;
    private List<jemput> jemputList;
    private RecyclerView recyclerView;
    private PenjemputanRecylerAdapter penjemputanRecylerAdapter;
    private TextView noPostEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_penjemputan);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser().getUid();

        noPostEntry = findViewById(R.id.ListNoPost);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        jemputList = new ArrayList<>();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onStart(){
        super.onStart();
        db.collection("jemput").whereEqualTo("userId", user).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    for(QueryDocumentSnapshot jemputs : queryDocumentSnapshots){
                        jemput jemput = jemputs.toObject(jemput.class);
                        jemputList.add(jemput);
                    }
                    penjemputanRecylerAdapter = new PenjemputanRecylerAdapter(HistoryPenjemputan.this, jemputList);
                    recyclerView.setAdapter(penjemputanRecylerAdapter);
                    penjemputanRecylerAdapter.notifyDataSetChanged();
                }else{
                    noPostEntry.setVisibility(View.VISIBLE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Keasalahan Apapun
                Toast.makeText(HistoryPenjemputan.this, "Ups! Ada yang Salah!" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
}