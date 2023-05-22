package com.example.sampahgo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import model.jemput;

public class Penjemputan extends AppCompatActivity {
    private static final int GALLERY_CODE = 1;
    private String[] items =  {"Kertas","Kaca","Elektronik","Besi dan Logam","Aluminium","Plastik"};
    private int[] points = {2, 2, 3, 3, 3, 4};

    int selectedPoints = 0;
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    EditText tanggal, waktu;

    private ImageView imageView;
    private Button saveButton;
    private ImageView tombollAddFoto;
    private EditText alamat;

    private String userSaatiniID;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private CollectionReference collectionReference = db.collection("jemput");

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjemputan);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = firebaseAuth.getInstance();
        alamat = findViewById(R.id.alamat);
        tanggal = findViewById(R.id.tanggal);
        waktu = findViewById(R.id.waktu);
        autoCompleteTxt = findViewById(R.id.kategori);
        imageView = findViewById(R.id.PenjemputanGambar);
        saveButton = findViewById(R.id.penjemputan_button);
        tombollAddFoto = findViewById(R.id.postTombolKamera);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, items);

        autoCompleteTxt.setAdapter(adapter);
        autoCompleteTxt.setThreshold(1); // jumlah karakter minimal yang harus dimasukkan sebelum dropdown muncul

        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        final String userId = user.getUid();

        Bundle extra = getIntent().getExtras();
        if(extra != null){
            userSaatiniID = extra.getString("userId");
            SimpanPenjemputan();
        }

        tombollAddFoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Mendapatkan Gambar dari Gallery
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });

        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,items);
        autoCompleteTxt.setAdapter(adapterItems);

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"Item: "+item,Toast.LENGTH_SHORT).show();
            }
        });

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                tanggal.setText(sdf.format(myCalendar.getTime()));
            }
        };

        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Penjemputan.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        waktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Penjemputan.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        waktu.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                SimpanPenjemputan();
                String selectedCategory = autoCompleteTxt.getText().toString();
//                int selectedPoints = 0;

                // mencari poin yang sesuai dengan kategori yang dipilih
                for (int i = 0; i < items.length; i++) {
                    if (items[i].equals(selectedCategory)) {
                        selectedPoints = points[i];
                        break;
                    }
                }

                // menampilkan jumlah poin yang didapatkan
                Toast.makeText(Penjemputan.this, "Anda mendapatkan " + selectedPoints + " poin untuk kategori " + selectedCategory, Toast.LENGTH_SHORT).show();

                // membuka halaman penukaran poin
                Intent intent = new Intent(Penjemputan.this, Penjemputan.class);
                intent.putExtra("POINTS", selectedPoints);
                startActivity(intent);

                db.collection("Users").whereEqualTo("userId", userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            String documentID = queryDocumentSnapshots.getDocuments().get(0).getId();

                            db.collection("Users").document(documentID).update("poin", FieldValue.increment(selectedPoints))
                                    .addOnSuccessListener(aVoid -> {
                                        // Pembaruan berhasil
                                        Toast.makeText(Penjemputan.this, "Poin penjemputan berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Pembaruan gagal
                                        Toast.makeText(Penjemputan.this, "Gagal memperbarui poin penjemputan", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                });
            }
        });
    }

    private void SimpanPenjemputan() {
        final String Kategori = autoCompleteTxt.getText().toString().trim();
        final String Alamat = alamat.getText().toString().trim();
        final String Tanggal = tanggal.getText().toString().trim();
        final String Waktu = waktu.getText().toString().trim();

        if (!TextUtils.isEmpty(Kategori) && !TextUtils.isEmpty(Alamat) && !TextUtils.isEmpty(Tanggal) && !TextUtils.isEmpty(Waktu) && imageUri != null) {
            final StorageReference filepath = storageReference.child("Penjemputan_images").child("penjemputan" + Timestamp.now().getNanoseconds());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        public void onSuccess(Uri uri) {
                            String imageUri = uri.toString();

                            jemput jemput = new jemput();
                            jemput.setKategori(Kategori);
                            jemput.setAlamat(Alamat);
                            jemput.setTanggal(Tanggal);
                            jemput.setWaktu(Waktu);
                            jemput.setImageUri(imageUri);
                            jemput.setTimeAdded(new Timestamp(new Date()));
                            jemput.setUserId(userSaatiniID);

                            collectionReference.add(jemput).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    startActivity(new Intent(Penjemputan.this, HistoryPenjemputan.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Gagal: " + e.getMessage(), Toast.LENGTH_SHORT);
                                }
                            });
                        }
                    });
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            if (data !=null){
                imageUri = data.getData(); //Mendapatkan Path Aktual
                imageView.setImageURI(imageUri); //Menampilkan Gambar
            }
        }
    }
}