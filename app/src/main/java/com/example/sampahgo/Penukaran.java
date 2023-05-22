package com.example.sampahgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import model.dataPenukaran;
import ui.dataPenukaranRecylerAdapter;

public class Penukaran extends AppCompatActivity {
    private TextView mPoinTextView;
    private FirebaseAuth auth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<dataPenukaran> dataPenukaranList;
    private RecyclerView recyclerView;
    private dataPenukaranRecylerAdapter penukaranRecylerAdapter;

    private CollectionReference collectionReference = db.collection("dataPenukaran");
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penukaran);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        final String userId = user.getUid();

        mPoinTextView = findViewById(R.id.poin);
        recyclerView = findViewById(R.id.PenukaranrecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataPenukaranList = new ArrayList<>();

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keMain = new Intent(Penukaran.this, ProsesPenukaran.class);
                keMain.putExtra("userId", userId);
            }
        });
        db.collection("Users").whereEqualTo("userId", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot  documentquery = task.getResult();
                    DocumentSnapshot document = documentquery.getDocuments().get(0);
                    // Mengubah integer menjadi string sebelum menampilkan
                    int poin = document.getLong("poin").intValue();
                    mPoinTextView.setText(String.valueOf(poin));
                }
            }
        });
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for(QueryDocumentSnapshot tukars : queryDocumentSnapshots){
                        dataPenukaran tukar = tukars.toObject(dataPenukaran.class);
                        tukar.setPenukaranID(tukars.getId());
                        dataPenukaranList.add(tukar);
                    }
                    penukaranRecylerAdapter = new dataPenukaranRecylerAdapter(Penukaran.this, dataPenukaranList);
                    recyclerView.setAdapter(penukaranRecylerAdapter);
                    penukaranRecylerAdapter.notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception e) {
                //Keasalahan Apapun
                Toast.makeText(Penukaran.this, "Ups! Ada yang Salah!" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
}