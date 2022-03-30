package com.example.institutoapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.institutoapp.Models.FCMBody;
import com.example.institutoapp.Models.FCMResponse;
import com.example.institutoapp.Models.MaestroModelo;
import com.example.institutoapp.Models.ReporteModelo;
import com.example.institutoapp.Providers.MaestroProvider;
import com.example.institutoapp.Providers.NotificationProvider;
import com.example.institutoapp.Providers.ReporteProvider;
import com.example.institutoapp.Providers.TokenProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.example.institutoapp.Padres.activity_principal_padre;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_detail_reporte extends AppCompatActivity {
    private ReporteProvider mReporteProvider;
    private MaestroProvider mMaestroProvider;
    private TokenProvider   mTokenProvider;
    private NotificationProvider mNotificationProvider;
    private TextView mtvTituloNotificacion, mtvDetalleNotificacion,
            mTvMaestro, mTvFecha, mTvUrgencia;
    private Button btnNotificationReaded;
    private ReporteModelo mExtraReporte;
    private Toolbar mToolbar;
    private ImageView icWarning;
    private DateFormat df;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_reporte);
        mToolbar = findViewById(R.id.Toolbar);
        mToolbar.setTitle("Detalle de Reporte");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolbar);
        showInfoDialow();
        mReporteProvider = new ReporteProvider();
        mMaestroProvider = new MaestroProvider();
        mTokenProvider   = new TokenProvider();
        mNotificationProvider = new NotificationProvider();
        icWarning = findViewById(R.id.ic_warningDetailReporte);
        icWarning.setVisibility(View.INVISIBLE);
        mExtraReporte = (ReporteModelo) getIntent().getExtras().getSerializable("Reporte");



        mReporteProvider.updateStatus(mExtraReporte.getIdReporte(), "Leido");
        mtvTituloNotificacion = findViewById(R.id.DetailActivitylblTituloReporte);
        mtvDetalleNotificacion = findViewById(R.id.DetailActivitylblDetalleReporte);
        mTvMaestro = findViewById(R.id.DetailActivitylblMaestro);
        mTvFecha = findViewById(R.id.DetailActivitylblFecha);
        mTvUrgencia = findViewById(R.id.DetailActivitylblUrgencia);
        btnNotificationReaded = findViewById(R.id.btnNotificationReadedDetailActivity);
        mtvTituloNotificacion.setText(mExtraReporte.getTipo());
        mtvDetalleNotificacion.setText(mExtraReporte.getDescripcion());
        if (mExtraReporte.getGravedad().trim().equals("Urgente")) {
            mTvUrgencia.setText("SE REQUIERE SU PRESENCIA DE INMEDIATO");
            icWarning.setVisibility(View.VISIBLE);
        }
        else if(mExtraReporte.getGravedad().trim().contains("Normal")) {
            icWarning.setImageResource(R.drawable.ic_atention);
            icWarning.setVisibility(View.VISIBLE);
            mTvUrgencia.setText("Por favor tome las medidas necesarias");
        }
        else{
            mTvUrgencia.setVisibility(View.INVISIBLE);
        }

        mTvFecha.setText(mExtraReporte.getFecha());
        btnNotificationReaded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationReaded(mExtraReporte.getMaestro_id());
            }
        });

    }

    private void notificationReaded(String maestroId) {
        try {
          if(!maestroId.equals("") || maestroId!=null){
              mTokenProvider.getToken(maestroId).addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                      if (snapshot.exists()){
                          mReporteProvider.updateStatus(mExtraReporte.getIdReporte(), "Enterado");
                          String token = snapshot.child("token").getValue().toString();
                          Map<String,String> data =  new HashMap<>();
                          data.put("title","El Padre ha sido notificado");
                          data.put("body","Ha leido el reporte y esta enterado");
                          data.put("idAlumno",mExtraReporte.getAlumno_id());


                          FCMBody fcmBody =  new FCMBody(token,"high",data);
                          mNotificationProvider.sendNotification(fcmBody).enqueue(new Callback<FCMResponse>() {
                              @Override
                              public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                  if(call.isExecuted()){
                                      Toast.makeText(getApplicationContext(), "Gracias por Notificar", Toast.LENGTH_SHORT).show();
                                      Intent i = new Intent(getApplicationContext(),activity_principal_padre.class);
                                      i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                      startActivity(i);
                                  }
                              }

                              @Override
                              public void onFailure(Call<FCMResponse> call, Throwable t) {
                                  Toast.makeText(getApplicationContext(), "Error al enviar la notificacion, por favor contacte a Soporte Tecnico", Toast.LENGTH_LONG).show();
                              }
                          });

                      }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {

                  }
              });
          }
        } catch (Exception e){
            System.out.println("-----------------------------------");
            System.out.println("Excepcion al notificar al maestro:"+e);
            System.out.println("-----------------------------------");
        }

    }

    public void showInfoDialow(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this) ;
        dialog.setTitle("Notificar de enterado");
            dialog.setMessage("Una vez lea la notificacion por favor presione el boton de enterado");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alert = dialog.create();
            alert.show();
        }
}