package com.example.sampahgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.user;

public class SignupActivity extends AppCompatActivity {

    private EditText signupfirstname, signuplastname, signupalamat, signupnotelp, signupEmail, signupPassword;
    private Button signupButton;
    private TextView loginRedirectText;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser userSaatini;
    private StorageReference storageReference;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        signupfirstname = findViewById(R.id.signup_firstname);
        signuplastname = findViewById(R.id.signup_lastname);
        signupalamat = findViewById(R.id.signup_alamat);
        signupnotelp = findViewById(R.id.signup_notelpon);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                userSaatini = firebaseAuth.getCurrentUser();
                if (userSaatini != null) {
                    //User Login
                } else {
                    //Belum Menjadi User
                }
            }
        };
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(signupfirstname.getText().toString()) && !TextUtils.isEmpty(signupPassword.getText().toString())){
                    String first = signupfirstname.getText().toString().trim();
                    String last = signuplastname.getText().toString().trim();
                    String fullname = first +  last;
                    String alamat = signupalamat.getText().toString().trim();
                    String notelp = signupnotelp.getText().toString().trim();
                    String email = signupEmail.getText().toString().trim();
                    String pass = signupPassword.getText().toString().trim();
                    CreateUserEmailAccount(first, last, email, pass, fullname, alamat, notelp);

                }else {
                    Toast.makeText(SignupActivity.this, "Ada sesuatu yang salah!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void CreateUserEmailAccount(String first, String last, String email,String pass, final String username, final String alamat, final String notelp) {
        if (!TextUtils.isEmpty(signupEmail.getText().toString()) && !TextUtils.isEmpty(signupPassword.getText().toString())) {
            firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //Membawa User ke Activity Berikut: Penjemputan dan Penukaran
                        userSaatini = firebaseAuth.getCurrentUser();
                        assert userSaatini != null;
                        final String userSaatiniId = userSaatini.getUid();

                        //Buat userMap sehingga kita dapat membuat user dalam user collection
                        Map<String, Object> userObj = new HashMap<>();
                        userObj.put("pertama", first);
                        userObj.put("terakhir", last);
                        userObj.put("userId", userSaatiniId);
                        userObj.put("username", username);
                        userObj.put("alamat", alamat);
                        userObj.put("notelp", notelp);
                        userObj.put("email", email);
                        userObj.put("foto", "https://firebasestorage.googleapis.com/v0/b/sampahgo-de705.appspot.com/o/AkunPengguna%2Fakunpengguna.png?alt=media&token=2ec006e9-87b3-48aa-9dfe-a4af33a5b813");
                        userObj.put("poin", 0);

                        //Menambahkan Pengguna ke FireStore
                        collectionReference.add(userObj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (Objects.requireNonNull(task.getResult().exists())) {
                                            String nama = task.getResult().getString("username");
                                            String alamat = task.getResult().getString("alamat");
                                            String notelp = task.getResult().getString("notelp");
                                            String email = task.getResult().getString("email");
                                            user penjemputanUser = user.getInstance();
                                            penjemputanUser.setUserID(userSaatiniId);
                                            penjemputanUser.setUsername(nama);
                                            penjemputanUser.setAlamat(alamat);
                                            penjemputanUser.setNotelp(notelp);
                                            penjemputanUser.setPertama(first);
                                            penjemputanUser.setTerakhir(last);
                                            penjemputanUser.setEmail(email);
                                            penjemputanUser.setFoto("https://firebasestorage.googleapis.com/v0/b/sampahgo-de705.appspot.com/o/AkunPengguna%2Fakunpengguna.png?alt=media&token=2ec006e9-87b3-48aa-9dfe-a4af33a5b813");
                                            penjemputanUser.setPoin("0");
                                        } else {

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Pesan Toast untuk informasi gagal registrasi
                                        Toast.makeText(SignupActivity.this, "Ada sesuatu yang salah!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        Toast.makeText(SignupActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                        i.putExtra("userId", userSaatiniId);
                        i.putExtra("username", username);
                        i.putExtra("alamat", alamat);
                        i.putExtra("notelp", notelp);
                        startActivity(i);
                    }
                }
            });
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        userSaatini = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}