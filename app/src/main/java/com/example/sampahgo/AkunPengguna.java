package com.example.sampahgo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.user;

public class AkunPengguna extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser userSaatini;
    private StorageReference storageReference;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");
    private TextView mPoinTextView;
    private int points;

    private EditText nama;
    private EditText alamat;
    private EditText notelp;
    private EditText email;
    private String userId;
    private TextView logout;
    private ImageView tombolkembali, fotoPengguna, editAkun;
    private Uri uri;
    private String lokasiFoto;
    private static final int GALLERY_CODE = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun_pengguna);

        nama = findViewById(R.id.namapengguna);
        alamat = findViewById(R.id.alamatpengguna);
        notelp = findViewById(R.id.nopengguna);
        email = findViewById(R.id.emailpengguna);
        logout = findViewById(R.id.logout);
        tombolkembali = findViewById(R.id.tombolkembali);
        fotoPengguna = findViewById(R.id.fotoPengguna);
        editAkun = findViewById(R.id.editakun);
        mPoinTextView = findViewById(R.id.point);

        storageReference = FirebaseStorage.getInstance().getReference();

        firebaseAuth = firebaseAuth.getInstance();

        Bundle extra = getIntent().getExtras();
        if(extra != null){
            userId = extra.getString("userId");
            loadProfile(userId);
        }

        tombolkembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateptofil(userId);
            }
        });

        fotoPengguna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                userSaatini = firebaseAuth.getCurrentUser();
            }
        };

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userSaatini != null && firebaseAuth != null){
                    firebaseAuth.signOut();
                    startActivity(new Intent(AkunPengguna.this, LoginActivity.class));
                }
            }
        });
    }

    private void loadProfile(String userId){
        collectionReference.whereEqualTo("userId", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot  documentquery = task.getResult();
                    DocumentSnapshot document = documentquery.getDocuments().get(0);
                    String foto = (String) document.get("foto");
                    nama.setText((String)document.get("username"));
                    alamat.setText((String)document.get("alamat"));
                    notelp.setText((String)document.get("notelp"));
                    email.setText((String)document.get("email"));

                    // Mengubah integer menjadi string sebelum menampilkan
                    int poin = document.getLong("poin").intValue();
                    mPoinTextView.setText(String.valueOf(poin));

                    uri = Uri.parse(foto);
                    Picasso.get().load(uri).into(fotoPengguna);
                }
            }
        });
    }

    private void updateptofil(String userId){
        final String Nama = nama.getText().toString().trim();
        final String Alamat = alamat.getText().toString().trim();
        final String Notelp = notelp.getText().toString().trim();
        final String Email = email.getText().toString().trim();
        collectionReference.whereEqualTo("userId", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot documentquery = task.getResult();
                    DocumentSnapshot document = documentquery.getDocuments().get(0);
                    String documentid = document.getId();
                    Map<String, Object> updatedata = new HashMap<>();
                    updatedata.put("username", Nama);
                    updatedata.put("alamat", Alamat);
                    updatedata.put("notelp", Notelp);
                    updatedata.put("email", Email);
                    collectionReference.document(documentid).update(updatedata).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AkunPengguna.this, "Berhasil Update Data", Toast.LENGTH_SHORT).show();
                            finish();
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
                uri = data.getData(); //Mendapatkan Path Aktual
                fotoPengguna.setImageURI(uri); //Menampilkan Gambar

                final StorageReference filepath = storageReference.child("AkunPengguna").child("Foto_" + nama.getText().toString() + "updateProfil_" + Timestamp.now().getNanoseconds());
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String foto = uri.toString();
                                String username = nama.getText().toString().trim();

                                Map<String,Object> updatefoto = new HashMap<>();
                                updatefoto.put("foto", foto);
                                collectionReference.whereEqualTo("username", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful() && !task.getResult().isEmpty()){
                                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                            String documentid = documentSnapshot.getId();
                                            collectionReference.document(documentid).update(updatefoto);
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        userSaatini = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(firebaseAuth != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}