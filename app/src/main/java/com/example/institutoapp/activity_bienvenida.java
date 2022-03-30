package com.example.institutoapp;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.institutoapp.Padres.activity_LoginPadre;
import com.example.institutoapp.maestros.activity_LoginMaestro;
import com.google.firebase.auth.FirebaseAuth;

public class activity_bienvenida extends AppCompatActivity {

    private Button btnMaestro, btnPadre;
    private int userType;
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);
        userType = 0;
        btnMaestro = (Button) findViewById(R.id.btnMaestro);
        btnPadre = (Button) findViewById(R.id.btnPadre);

        mPref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);
        final SharedPreferences.Editor editor = mPref.edit();

        btnMaestro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userType = 1;
                editor.putString("user", "maestro");
                editor.apply();
                lanzar(userType);
            }
        });
        btnPadre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userType = 0;
                editor.putString("user", "padre");
                editor.apply();
                lanzar(userType);
            }
        });

    }



    private void verifyUser() {
        try {
            String userType = mPref.getString("user", "");
            if (userType.equals("maestro")) {
               Intent i = new Intent(activity_bienvenida.this, activity_LoginMaestro.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            } else if (userType.equals("padre")) {
              Intent i = new Intent(activity_bienvenida.this, activity_LoginPadre.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }

        } catch (Exception e) {
            Log.e("Error en OnStart", e.toString());
        }
    }

    private void lanzar(int userType) {
        Intent i = new Intent();
        if (userType == 1) {
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i = new Intent(getApplicationContext(), activity_LoginMaestro.class);
        } else {
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i = new Intent(getApplicationContext(), activity_LoginPadre.class);
        }
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            verifyUser();

        } catch (Exception e) {
            Log.e("Error OnStart", "Error" + e);
        }

    }


}