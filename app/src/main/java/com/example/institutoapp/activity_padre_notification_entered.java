package com.example.institutoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.institutoapp.maestros.activity_PrincipalMaestro;
import com.example.institutoapp.Models.ReporteModelo;
import com.example.institutoapp.Providers.AlumnoProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class activity_padre_notification_entered extends AppCompatActivity {
    private AlumnoProvider mAlumnoProvider;

    private LottieAnimationView animationView;
    private ReporteModelo mExtraReporte;
    private TextView txtNombreAlumno;
    private Button btnRegresar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padre_notification_entered);
        mAlumnoProvider =  new AlumnoProvider();
        mExtraReporte = (ReporteModelo) getIntent().getExtras().getSerializable("Reporte");
        animationView =  findViewById(R.id.img_AnimationNotification);
        animationView.setAnimation(R.raw.notification_lottie);
        animationView.playAnimation();
        btnRegresar = findViewById(R.id.btnRegresarNotificationReaded);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity_padre_notification_entered.this,activity_PrincipalMaestro.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
        });
        txtNombreAlumno = findViewById(R.id.txtPadreNotificado_NombreAlumno);


            mAlumnoProvider.getUser(mExtraReporte.getAlumno_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        txtNombreAlumno.setText(snapshot.child("nombre").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



    }
}