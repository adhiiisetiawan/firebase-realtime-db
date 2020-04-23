package com.oxcart.firebaserealtimedb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private EditText editNIM;
    private EditText editNama;
    private Button btnAdd;

    private RecyclerView recyclerView;
    private MahsiswaAdapter mahasiswaAdapater;
    private ArrayList<Mahasiswa> mahasiswaArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference("mahasiswa");

        editNIM = findViewById(R.id.edit_text_nim);
        editNama = findViewById(R.id.edit_text_nama);
        btnAdd = findViewById(R.id.btn_tambah_data);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mahasiswaAdapater = new MahsiswaAdapter(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mahasiswaArrayList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Mahasiswa mahasiswa = postSnapshot.getValue(Mahasiswa.class);

                    mahasiswaArrayList.add(mahasiswa);
                }

                mahasiswaAdapater.addItem(mahasiswaArrayList);
                recyclerView.setAdapter(mahasiswaAdapater);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void insertData(){
        String nim = editNIM.getText().toString();
        String nama = editNama.getText().toString();

        if (!TextUtils.isEmpty(nim) && !TextUtils.isEmpty(nama)){
            Mahasiswa mahasiswa = new Mahasiswa(nim,nama);

            databaseReference.child(nim).setValue(mahasiswa);

            editNIM.setText("");
            editNama.setText("");

            Toast.makeText(this, "Mahasiswa Added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Enter NIM and Name", Toast.LENGTH_SHORT).show();
        }
    }

}
