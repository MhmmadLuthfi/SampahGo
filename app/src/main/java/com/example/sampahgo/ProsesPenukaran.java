package com.example.sampahgo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import model.tukar;
import model.dataPenukaran;

public class ProsesPenukaran extends AppCompatActivity {
    private static final int GALLERY_CODE = 1;
    TextView NamaDataPenukaran, DeskripsiDataPenukaran, PointDataPenukaran;
    EditText AlamatPenggunaPenukaran, NohpPenggunaPenukaran;
    ImageView ImageDataPenukaran;

    private Uri ImageUri;
    Button penukaran_button;
    private String gambar;

    private String userSaatiniID;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private final CollectionReference collectionReference = db.collection("tukar");
    private TextView mPoinTextView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_penukaran);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        NamaDataPenukaran = findViewById(R.id.NamaDataPenukaran);
        DeskripsiDataPenukaran = findViewById(R.id.DeskripsiDataPenukaran);
        PointDataPenukaran = findViewById(R.id.PointDataPenukaran);
        AlamatPenggunaPenukaran = findViewById(R.id.AlamatPenggunaPenukaran);
        NohpPenggunaPenukaran = findViewById(R.id.NohpPenggunaPenukaran);
        ImageDataPenukaran = findViewById(R.id.ImageDataPenukaran);
        penukaran_button = findViewById(R.id.penukaran_button);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        final String userId = user.getUid();
        mPoinTextView = findViewById(R.id.PointPengguna);

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
        ImageDataPenukaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });

        Intent intent = getIntent();
            userSaatiniID = intent.getStringExtra("userID");
            String penukaranID = intent.getStringExtra("PenukaranID");
            NamaDataPenukaran.setText(intent.getStringExtra("namaPenukaran"));
            DeskripsiDataPenukaran.setText(intent.getStringExtra("deskripsiPenukaran"));
            PointDataPenukaran.setText(intent.getStringExtra("pointPenukaran"));
            Glide.with(getApplicationContext()).load(intent.getStringExtra("imageUri")).into(ImageDataPenukaran);


            gambar = intent.getStringExtra("imageUri");

        penukaran_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mengambil nilai poin pengguna saat ini
                int poinPengguna = Integer.parseInt(mPoinTextView.getText().toString());

                // Mengambil nilai poin dari data penukaran
                int poinPenukaran = Integer.parseInt(PointDataPenukaran.getText().toString());

                // Memeriksa apakah pengguna memiliki cukup poin untuk melakukan penukaran
                if (poinPengguna >= poinPenukaran) {
                    // Mengurangi poin pengguna dengan poin penukaran
                    int poinSisa = poinPengguna - poinPenukaran;

                    // Melakukan proses penukaran
                    SimpanPenukaran();

                    // Mengupdate poin pengguna yang tersisa
                    UpdatePoinPengguna(poinSisa);
                } else {
                    Toast.makeText(getApplicationContext(), "Poin tidak mencukupi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void SimpanPenukaran(){
        final String namaPenukaranPengguna = NamaDataPenukaran.getText().toString().trim();
        final String deskripsiPenukaranPengguna = DeskripsiDataPenukaran.getText().toString().trim();
        final String pointPenukaranPengguna = PointDataPenukaran.getText().toString().trim();
        final String alamatPenukaranPengguna = AlamatPenggunaPenukaran.getText().toString().trim();
        final String nohpPenukaranPengguna = NohpPenggunaPenukaran.getText().toString().trim();

        if (!namaPenukaranPengguna.isEmpty() && !deskripsiPenukaranPengguna.isEmpty() && !pointPenukaranPengguna.isEmpty() && !alamatPenukaranPengguna.isEmpty() && !nohpPenukaranPengguna.isEmpty() && ImageUri != null) {
            final StorageReference filepath = storageReference.child("PenukaranPengguna_images").child("Penukaran" + Timestamp.now().getNanoseconds());
            filepath.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        public void onSuccess(Uri uri) {
                            String ImageUri = uri.toString();

                            tukar tukar = new tukar();
                            tukar.setnamaPenukaran(namaPenukaranPengguna);
                            tukar.setDeskripsiPenukaran(deskripsiPenukaranPengguna);
                            tukar.setPointPenukaran(pointPenukaranPengguna);
                            tukar.setAlamatPenukaran(alamatPenukaranPengguna);
                            tukar.setNohpPenukaran(nohpPenukaranPengguna);
                            tukar.setImageUri(ImageUri);
                            tukar.setTimeAdded(new Timestamp(new Date()));
                            tukar.setPenggunaID(userSaatiniID);

                            collectionReference.add(tukar).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    startActivity(new Intent(ProsesPenukaran.this, HistoryPenukaran.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });
        } else if (!namaPenukaranPengguna.isEmpty() && !deskripsiPenukaranPengguna.isEmpty() && !pointPenukaranPengguna.isEmpty() && !alamatPenukaranPengguna.isEmpty() && !nohpPenukaranPengguna.isEmpty() && ImageUri == null) {
            tukar tukar = new tukar();

            tukar.setnamaPenukaran(namaPenukaranPengguna);
            tukar.setDeskripsiPenukaran(deskripsiPenukaranPengguna);
            tukar.setPointPenukaran(pointPenukaranPengguna);
            tukar.setAlamatPenukaran(alamatPenukaranPengguna);
            tukar.setNohpPenukaran(nohpPenukaranPengguna);
            tukar.setTimeAdded(new Timestamp(new Date()));
            tukar.setImageUri(gambar);
            tukar.setPenggunaID(userSaatiniID);

            collectionReference.add(tukar).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    startActivity(new Intent(   ProsesPenukaran.this, HistoryPenukaran.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
        }
    }
    private void UpdatePoinPengguna(int poinBaru) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        final String userId = user.getUid();

        // Mengupdate nilai poin pengguna di Firestore
        db.collection("Users").whereEqualTo("userId", userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    String documentID = queryDocumentSnapshots.getDocuments().get(0).getId();

                    db.collection("Users").document(documentID)
                        .update("poin", poinBaru)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Poin pengguna berhasil diperbarui
                                // Lakukan sesuatu jika diperlukan
                                Toast.makeText(getApplicationContext(), "Berhasil memperbarui poin pengguna", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Penanganan kegagalan saat memperbarui poin pengguna
                                Toast.makeText(getApplicationContext(), "Gagal memperbarui poin pengguna", Toast.LENGTH_SHORT).show();
                            }
                        });
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            if (data !=null){
                ImageUri = data.getData(); //Mendapatkan Path Aktual
                ImageDataPenukaran.setImageURI(ImageUri); //Menampilkan Gambar
            }
        }
    }
}