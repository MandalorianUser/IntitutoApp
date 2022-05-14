package com.example.institutoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.institutoapp.Models.AlumnoModelo;
import com.example.institutoapp.Providers.AlumnoProvider;
import com.example.institutoapp.Providers.ReporteProvider;
import com.example.institutoapp.RecyclerViews.RecyclerViewAdapterHijosPersonalizado;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class activity_hijo_detalle extends AppCompatActivity {
    ReporteProvider mReporteProvider;

String reportes;
TextView txtReportes,txtNombre;
AlumnoModelo hijoExtra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hijo_detalle);
        mReporteProvider = new ReporteProvider();
        txtReportes = findViewById(R.id.txtDetalleHijoNRepoprtes);
        txtNombre = findViewById(R.id.txtDetalleHijoNombre);
        recibirDatosHijo();
        txtNombre.setText(hijoExtra.getNombre().toUpperCase(Locale.ROOT));
        listarReportes(hijoExtra.getId());

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
    private void listarReportes(String idAlumno) {
        System.out.println("-------------------------------");
        System.out.println("-------------------------------");
        System.out.println("Id Alumno:"+idAlumno);

            mReporteProvider.getReportesAlumno(idAlumno).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        Toast.makeText(activity_hijo_detalle.this, "Existen reportes de este alumno", Toast.LENGTH_SHORT).show();
                        System.out.println("-----------------------------------------");
                        System.out.println("Reportes: "+snapshot.toString());
                        System.out.println("-----------------------------------------");
                    }else
                        Toast.makeText(activity_hijo_detalle.this, "Este alumno no tiene reportes", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

