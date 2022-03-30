package com.example.institutoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import de.hdodenhof.circleimageview.CircleImageView;

public class activity_escolaridad extends AppCompatActivity { private CardView primaria, preescolar, secundaria, prepa;
    private CircleImageView btnAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolaridad);
        btnAtras = findViewById(R.id.btnAtrasEscolaridadActivity);
        primaria = findViewById(R.id.card_view_primaria);
        preescolar = findViewById(R.id.card_view_preescolar);
        secundaria = findViewById(R.id.card_view_secundaria);
        prepa =  findViewById(R.id.card_view_preparatoria);

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        prepa = findViewById(R.id.card_view_preparatoria);
        primaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity_escolaridad.this, activity_grupos.class);
                i.putExtra("Escolaridad", "Primaria");
                startActivity(i);
            }
        });
        preescolar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity_escolaridad.this, activity_grupos.class);
                i.putExtra("Escolaridad", "Preescolar");
                startActivity(i);
            }
        });
        secundaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity_escolaridad.this, activity_grupos.class);
                i.putExtra("Escolaridad", "Secundaria");
                i.putExtra("funciona",true);
                startActivity(i);
            }
        });
        prepa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity_escolaridad.this, activity_grupos.class);
                i.putExtra("Escolaridad", "Preparatoria");
                startActivity(i);
            }
        });
    }
}