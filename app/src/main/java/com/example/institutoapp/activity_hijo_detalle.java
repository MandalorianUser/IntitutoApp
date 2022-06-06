package com.example.institutoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.institutoapp.Models.AlumnoModelo;
import com.example.institutoapp.Models.ReporteModelo;
import com.example.institutoapp.Providers.AlumnoProvider;
import com.example.institutoapp.Providers.ReporteProvider;
import com.example.institutoapp.RecyclerViews.RecyclerViewAdapterHijosPersonalizado;
import com.example.institutoapp.RecyclerViews.ReportesHijosAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class activity_hijo_detalle extends AppCompatActivity {
    private ReporteProvider mReporteProvider;

    private RecyclerView mRecyclerView;
    private ReportesHijosAdapter mAdapter;

    private String reportes;
    private TextView txtReportes, txtNombre;
    private AlumnoModelo hijoExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hijo_detalle);
        mReporteProvider = new ReporteProvider();

        mRecyclerView = findViewById(R.id.RecyclerViewDetalleHijo);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        txtNombre = findViewById(R.id.txtDetalleHijoNombre);
        recibirDatosHijo();
        txtNombre.setText(hijoExtra.getNombre().toUpperCase(Locale.ROOT));

    }

    private void recibirDatosHijo() {
        try {

            hijoExtra = (AlumnoModelo) getIntent().getExtras().getSerializable("hijoExtra");


        } catch (Exception e) {
            Log.e("HijoDetalle", e.getMessage());
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        Query query = FirebaseDatabase.getInstance().getReference()
                .child("Reportes")
                .orderByChild("alumno_id")
                .equalTo(hijoExtra.getId());
        FirebaseRecyclerOptions<ReporteModelo> options = new FirebaseRecyclerOptions.Builder<ReporteModelo>()
                .setQuery(query, ReporteModelo.class)
                .build();
        mAdapter = new ReportesHijosAdapter(options, activity_hijo_detalle.this);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}

