package com.example.institutoapp.maestros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.institutoapp.Models.ReporteModelo;
import com.example.institutoapp.Providers.AuthProvider;
import com.example.institutoapp.Providers.MaestroProvider;
import com.example.institutoapp.Providers.TokenProvider;
import com.example.institutoapp.R;
import com.example.institutoapp.RecyclerViews.RecylerViewFirebasePrincipalMaestro;
import com.example.institutoapp.activity_reporte;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class activity_PrincipalMaestro extends AppCompatActivity {
    private ArrayList<ReporteModelo> listReporte = new ArrayList<ReporteModelo>();
    private TokenProvider mTokenProvider;
    private RecyclerView mRecyclerViewReportes;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FloatingActionButton btnNuevo;
    private AuthProvider mAuthProvider;
    private MaestroProvider mMaestroProvider;
    private CircleImageView mFotoUser;
    private RecylerViewFirebasePrincipalMaestro mAdapter;
    private TextView lblTitulo;
    private Toolbar mToolbar;
    private SharedPreferences mMaestroPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_maestro);
        FirebaseApp.initializeApp(this);
        mMaestroProvider = new MaestroProvider();
        mFotoUser =  findViewById(R.id.imgMaestroPrincipal);
        mAuthProvider =  new AuthProvider();
        mTokenProvider = new TokenProvider();
        mToolbar = findViewById(R.id.Toolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolbar);



        mRecyclerViewReportes= findViewById(R.id.RecyclerViewMaestroReportes);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        lblTitulo = findViewById(R.id.lblNombreMaestroLogeado);
        genereteToken();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewReportes.setLayoutManager(linearLayoutManager);
        listarDatos();
        getImageUser();
        btnNuevo=(FloatingActionButton)findViewById(R.id.btnCrearReporte2);
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), activity_reporte.class);
                i.putExtra("maestroNombre",mMaestroPreferences.getString("Nombre",""));
                startActivity(i);
            }
        });
    }

    private void getImageUser() {
        mMaestroProvider.getUser(mAuthProvider.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String imgUserUrl = snapshot.child("imagen").getValue().toString();
                    String userName =  snapshot.child("nombre").getValue().toString();
                    mMaestroPreferences  = getSharedPreferences("Maestro",MODE_PRIVATE);
                    SharedPreferences.Editor editor  = mMaestroPreferences.edit();
                    editor.putString("Nombre",userName);
                    editor.apply();
                    Picasso.with(activity_PrincipalMaestro.this).load(imgUserUrl).into(mFotoUser);
                    lblTitulo.setText(userName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void listarDatos() {
        databaseReference.child("Reportes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listReporte.clear();
                for(DataSnapshot objSnaptdhot : snapshot.getChildren()){
                    ReporteModelo r=objSnaptdhot.getValue(ReporteModelo.class);
                    listReporte.add(r);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void genereteToken(){
        mTokenProvider.create(mAuthProvider.getId());

    }

    private void logout(){
        mAuthProvider.logout();
        Intent i = new Intent(activity_PrincipalMaestro.this,activity_LoginMaestro.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.itemLogout){
            Toast.makeText(getApplicationContext(),"Cerrando sesion",Toast.LENGTH_SHORT).show();
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            Query query = FirebaseDatabase.getInstance().getReference()
                    .child("Reportes")
                    .orderByChild("maestro_id")
                    .equalTo(mAuthProvider.getId());

            FirebaseRecyclerOptions<ReporteModelo> options =  new FirebaseRecyclerOptions.Builder<ReporteModelo>()
                    .setQuery(query,ReporteModelo.class).build();
            mAdapter = new RecylerViewFirebasePrincipalMaestro(options, activity_PrincipalMaestro.this);
            mRecyclerViewReportes.setAdapter(mAdapter);
            mAdapter.startListening();
        }catch (Exception e){
            Log.e("Error Onstart", "onStart: "+e.getMessage());
        }

    }


}