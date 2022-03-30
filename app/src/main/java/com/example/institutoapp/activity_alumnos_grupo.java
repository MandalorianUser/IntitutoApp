package com.example.institutoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.institutoapp.Models.AlumnoModelo;
import com.example.institutoapp.Providers.AlumnoProvider;
import com.example.institutoapp.RecyclerViews.RecyclerViewAdaptador;
import com.example.institutoapp.Utils.ToastHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class activity_alumnos_grupo extends AppCompatActivity {
    ArrayList<AlumnoModelo> alumnosList =  new ArrayList<AlumnoModelo>();
    AlumnoModelo a ;

    ToastHelper toastHelper =  new ToastHelper();
    String Escolaridad="",GrupoId = "";
    RecyclerView recyclerViewAdaptadorAlumnos;
    DatabaseReference mDatabase;
    AlumnoProvider mAlumnoProvider;
    TextView lblNombreGrupo;
    CircleImageView btnAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumnos_grupo);
        mAlumnoProvider =  new AlumnoProvider();
        btnAtras = findViewById(R.id.btnAtrasGruposActivity);
        lblNombreGrupo = findViewById(R.id.lblNombreGrupoRecyclerView);
        a = new AlumnoModelo();
        recyclerViewAdaptadorAlumnos = findViewById(R.id.RecyclerViewAlumnosGrupo);
        recyclerViewAdaptadorAlumnos.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference();


        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity_alumnos_grupo.this,activity_grupos.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("Escolaridad",Escolaridad);
                startActivity(i);
            }
        });

    }

    private void listarDatos(String Escolaridad, String GrupoId){

        try{


            mDatabase.child("Grupos").child(Escolaridad).child(GrupoId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        final String alumnos = (snapshot.child("alumnos").getValue().toString());
                        String[] alumnosId = alumnos.split("-");
                        for (int i = 0; i < alumnosId.length; i++) {
                            mAlumnoProvider.getUser(alumnosId[i]).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        AlumnoModelo a = snapshot.getValue(AlumnoModelo.class);
                                        alumnosList.add(a);

                                    }
                                    RecyclerViewAdaptador adapter = new RecyclerViewAdaptador(alumnosList,getApplicationContext());
                                    recyclerViewAdaptadorAlumnos.setAdapter(adapter);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });




                        }




                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(),"No se pudieron obtener los datos",Toast.LENGTH_SHORT).show();
                }


            });
        } catch (Exception e){
            Log.e("Error", e.getMessage());
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        try {
            Escolaridad = getIntent().getStringExtra("Escolaridad");
            GrupoId = getIntent().getStringExtra("GrupoId");
            lblNombreGrupo.setText(getIntent().getStringExtra("NombreGrupo"));
            if(!Escolaridad.equals("") && !GrupoId.equals("")){
                listarDatos(Escolaridad,GrupoId);
            }else{
                toastHelper.LanzarToast("No se recibierion los parametros",0,getApplicationContext(), Gravity.NO_GRAVITY);
            }

        }catch (Exception e){
            Log.d("Error",e.getMessage());
        }
    }
}