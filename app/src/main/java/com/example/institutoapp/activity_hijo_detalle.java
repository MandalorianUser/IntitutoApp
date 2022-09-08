package com.example.institutoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class activity_hijo_detalle extends AppCompatActivity {
private TextView txtEstadoActual,txtEstadoActualN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hijo_detalle);
        txtEstadoActual = findViewById(R.id.txtDetalleHijoEstadoActual);
        txtEstadoActualN = findViewById(R.id.txtDetalleHijoEstadoActualN);
        txtEstadoActualN.setText("2");
        txtEstadoActual.setText("Activo");
    }
}