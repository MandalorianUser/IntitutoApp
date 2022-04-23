package com.example.institutoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.institutoapp.Models.AlumnoModelo;

import java.util.Locale;

public class activity_hijo_detalle extends AppCompatActivity {
String reportes;
TextView txtReportes,txtNombre;
AlumnoModelo hijoExtra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hijo_detalle);
        txtReportes = findViewById(R.id.txtDetalleHijoNRepoprtes);
        txtNombre = findViewById(R.id.txtDetalleHijoNombre);
        recibirDatosHijo();
        txtNombre.setText(hijoExtra.getNombre().toUpperCase(Locale.ROOT));

    }

    private void recibirDatosHijo() {
        try {

            hijoExtra = (AlumnoModelo) getIntent().getExtras().getSerializable("hijoExtra");
            System.out.println("------------------------------------------");
            System.out.println("Desde el OnStart Hijo Detalle "+hijoExtra.toString());
            System.out.println("------------------------------------------");

        }catch (Exception e){
            Log.e("HijoDetalle",e.getMessage());
        }
    }

}