package com.example.institutoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.institutoapp.Models.GrupoModel;
import com.example.institutoapp.Providers.GrupoProvider;
import com.example.institutoapp.RecyclerViews.RecyclerViewsFirebase;
import com.example.institutoapp.Utils.ToastHelper;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class activity_grupos extends AppCompatActivity {
    private RecyclerView recyclerViewGrupos;
    private RecyclerViewsFirebase mAdapter;
    private CircleImageView btnAtras;
    ArrayList<GrupoModel> gruposList;
    private String Escolaridad = "";
    private TextView lblEscolaridad;
    DatabaseReference databaseReference;
    GrupoProvider mGrupoProvider;
    ToastHelper toastHelper;
    SharedPreferences mPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos);
        lblEscolaridad = findViewById(R.id.lblEscolaridadActivityGrupos);
        try {
            mPref = getApplicationContext().getSharedPreferences("Escolaridad", MODE_PRIVATE);
            final SharedPreferences.Editor editor = mPref.edit();
            editor.putString("Escolaridad", Escolaridad);
            editor.apply();
            if (getIntent().getStringExtra("Escolaridad").equals("") || getIntent().getStringExtra("Escolaridad") == null) {
                Escolaridad = mPref.getString("Escolaridad", "");
                lblEscolaridad.setText(Escolaridad);

            } else {
                Escolaridad = getIntent().getStringExtra("Escolaridad");
                lblEscolaridad.setText(Escolaridad);
            }
        } catch (Exception e) {
            e.getMessage();
        }


        toastHelper = new ToastHelper();
        mGrupoProvider = new GrupoProvider();
        //MyToolbar.show(this,"Regresar",true);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        gruposList = new ArrayList<GrupoModel>();

        recyclerViewGrupos = (RecyclerView) findViewById(R.id.RecyclerViewGrupos);
        recyclerViewGrupos.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewGrupos.setLayoutManager(linearLayoutManager);


        btnAtras = findViewById(R.id.btnAtrasGruposActivity);
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity_grupos.this, activity_escolaridad.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        try {
            listarDatos();


        } catch (Exception e) {
            Log.d("ErrorAGrupos", e.getMessage());
            System.out.println("-----------------------------");
            System.out.println("Error de grupo a buscar ");

        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        //mAdapter.stopListening();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listarDatos();

    }

    public void listarDatos() {
        if (Escolaridad != "" && !Escolaridad.isEmpty()) {
            Query query = FirebaseDatabase.getInstance().getReference()
                    .child("Grupos")
                    .child(Escolaridad);
            FirebaseRecyclerOptions<GrupoModel> options = new FirebaseRecyclerOptions.Builder<GrupoModel>()
                    .setQuery(query, GrupoModel.class)
                    .build();
            mAdapter = new RecyclerViewsFirebase(options, activity_grupos.this, Escolaridad);
            recyclerViewGrupos.setAdapter(mAdapter);
            mAdapter.startListening();
        } else {
            toastHelper.LanzarToast("La escolaridad esta vacia", 1, getApplicationContext(), Gravity.BOTTOM);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        listarDatos();
    }
}