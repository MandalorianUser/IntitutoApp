package com.example.institutoapp.Padres;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.institutoapp.Models.AlumnoModelo;
import com.example.institutoapp.Providers.AlumnoProvider;
import com.example.institutoapp.Providers.AuthProvider;
import com.example.institutoapp.Providers.PadreProvider;
import com.example.institutoapp.Providers.TokenProvider;
import com.example.institutoapp.R;
import com.example.institutoapp.RecyclerViews.RecyclerViewAdapderHijos;
import com.example.institutoapp.RecyclerViews.RecyclerViewAdaptador;
import com.example.institutoapp.RecyclerViews.RecyclerViewAdapterHijosPersonalizado;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class activity_principal_padre extends AppCompatActivity {
    TokenProvider mTokenProvider;
    AuthProvider mAuthProvider;
    Toolbar mToolbar;
    private PadreProvider mPadreProvider;
    private CircleImageView mFotoUser;
    private String hijosPadre;
    private TextView lblTitulo;
    private RecyclerView recyclerViewHijos;
    private RecyclerViewAdapderHijos mAdapter;
    private AlumnoProvider mAlumnoProvider;
    private ArrayList<AlumnoModelo> alumnosList =  new java.util.ArrayList<AlumnoModelo>();


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_padre);
        mTokenProvider = new TokenProvider();
        mAuthProvider = new AuthProvider();
        mPadreProvider = new PadreProvider();
        mAlumnoProvider =  new AlumnoProvider();
        lblTitulo = findViewById(R.id.lblNombrePadreLogeado);
        mFotoUser = findViewById(R.id.imgPadrePrincipal);
        generateToken();
        getImageUser();
        mToolbar = findViewById(R.id.Toolbar);
        mToolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(mToolbar);
        recyclerViewHijos = findViewById(R.id.recyclerViewHijos);
        recyclerViewHijos.setLayoutManager(new LinearLayoutManager(this));


    }




    private void generateToken() {
        mTokenProvider.create(mAuthProvider.getId());
    }


    private void getImageUser() {
        try {
            mPadreProvider.getUser(mAuthProvider.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String imgUserUrl  =  snapshot.child("imagen").getValue().toString();
                        String userName    =    snapshot.child("nombre").getValue().toString();
                        if (snapshot.hasChild("hijos")){
                            hijosPadre = snapshot.child("hijos").getValue().toString();
                            if (!hijosPadre.equals("")){
                                listarHijos(hijosPadre); }
                            else{
                                Toast.makeText(getApplicationContext(), "No tiene hijos", Toast.LENGTH_SHORT).show();
                            }
                        }
                       Picasso.with(activity_principal_padre.this).load(imgUserUrl).into(mFotoUser);
                        lblTitulo.setText(userName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch (Exception e){
            System.out.println("------------------------------------------------------");
            System.out.println("Error al obtener los datos del padre "+e);
            System.out.println("------------------------------------------------------");

        }
    }

    private void logout() {
        mAuthProvider.logout();
        Intent i = new Intent(activity_principal_padre.this, activity_LoginPadre.class);
        startActivity(i);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemLogout) {
            Toast.makeText(getApplicationContext(), "Cerrando sesion", Toast.LENGTH_SHORT).show();
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

    }

    private void listarHijos(String hijosPadre) {
        String[] hijos =  hijosPadre.split("-");
        System.out.println("Hijos:"+hijos);
        for (String hijo:hijos) {
            mAlumnoProvider.getUser(hijo).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if(snapshot.exists()){
                       AlumnoModelo a = snapshot.getValue(AlumnoModelo.class);
                       alumnosList.add(a);
                   }

                    RecyclerViewAdapterHijosPersonalizado adapter = new RecyclerViewAdapterHijosPersonalizado (alumnosList,getApplicationContext());
                    recyclerViewHijos.setAdapter(adapter);
                }



                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
