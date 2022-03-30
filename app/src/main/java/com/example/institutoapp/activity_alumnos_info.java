package com.example.institutoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


import com.example.institutoapp.Models.AlumnoModelo;
import com.example.institutoapp.RecyclerViews.RecyclerViewAdaptador;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class activity_alumnos_info extends AppCompatActivity {
    private RecyclerView recyclerViewAlumno;
    TextView txtPrueba;
    List<AlumnoModelo> alumno;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumnos_info);
        FirebaseApp.initializeApp(this);
        alumno=new ArrayList<>();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        recyclerViewAlumno=(RecyclerView)findViewById(R.id.RecyclerViewAlumno);
        recyclerViewAlumno.setLayoutManager(new LinearLayoutManager(this));
        alumno=listarDatos();





    }


    private  List<AlumnoModelo> listarDatos() {

        databaseReference.child("Alumnos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    AlumnoModelo a = objSnaptshot.getValue(AlumnoModelo.class);

                    alumno.add(a);


                }
                RecyclerViewAdaptador adapter = new RecyclerViewAdaptador(alumno,getApplicationContext());
                recyclerViewAlumno.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"No",Toast.LENGTH_LONG).show();
            }
        });
        return alumno;
    }


}