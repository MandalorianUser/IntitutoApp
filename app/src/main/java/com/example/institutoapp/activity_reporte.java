package com.example.institutoapp;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import com.example.institutoapp.Models.AlumnoModelo;
import com.example.institutoapp.Models.FCMBody;
import com.example.institutoapp.Models.FCMResponse;
import com.example.institutoapp.Models.PadreModelo;
import com.example.institutoapp.Models.ReporteModelo;
import com.example.institutoapp.Providers.AlumnoProvider;
import com.example.institutoapp.Providers.AuthProvider;
import com.example.institutoapp.Providers.NotificationProvider;
import com.example.institutoapp.Providers.PadreProvider;
import com.example.institutoapp.Providers.ReporteProvider;
import com.example.institutoapp.Providers.TokenProvider;
import com.example.institutoapp.Utils.GeneratorId;
import com.example.institutoapp.Utils.ToastHelper;
import com.example.institutoapp.maestros.activity_PrincipalMaestro;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.N)
public class activity_reporte extends AppCompatActivity {
    private TokenProvider mTokenProvider;
    private NotificationProvider mNotificationProvider;
    private ReporteProvider mReporteProvider;
    private AuthProvider mAuthProvider;
    private PadreProvider mPadreProvider;
    private ReporteModelo reporteModelo;
    private GeneratorId mGenerateId;
    private ToastHelper toastHelper;
    private String padreId, descripcionReporte, tipoReporte;
    private EditText txtIdAlumno, txtDescripcion;
    private RadioButton rdbtnAcademico, rdbtnDiciplinario;
    private AlumnoModelo alumnoModelo;
    private AlumnoProvider mAlumnoProvider;
    private SimpleDateFormat df = new SimpleDateFormat("dd/MMM/YYYY");
    private Date fecha = new Date();
    private String fechaActual = df.format(fecha);
    private AutoCompleteTextView txtUrgencia;
    private PadreModelo p;
    private SharedPreferences mPadrePreferences;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);
        alumnoModelo = new AlumnoModelo();
        toastHelper = new ToastHelper();
        mAlumnoProvider = new AlumnoProvider();
        txtIdAlumno = findViewById(R.id.txtReporteIdALumno);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        rdbtnAcademico = findViewById(R.id.radioButtonAcademico);
        rdbtnDiciplinario = findViewById(R.id.radioButtonDiciplinario);
        mNotificationProvider = new NotificationProvider();
        mAuthProvider = new AuthProvider();
        mTokenProvider = new TokenProvider();
        mPadreProvider = new PadreProvider();
        reporteModelo = new ReporteModelo();
        mReporteProvider = new ReporteProvider();
        mGenerateId = new GeneratorId();

        txtUrgencia =  findViewById(R.id.txtAutocompleteLayoutUrgenciaReporte);
        Button btnReportar = findViewById(R.id.btnReportarAlumno);
        CircleImageView btnAtras = findViewById(R.id.btnAtrasReporteActivity);
        AutoCompleteTextView dropMenu = findViewById(R.id.txtAutocompleteLayoutUrgenciaReporte);
        String[] opciones =  {"Baja", "Normal","Urgente"};
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,R.layout.dropdown_item,opciones);
        dropMenu.setAdapter(adapter);

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity_reporte.this, activity_PrincipalMaestro.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        ImageView btnBuscar = (ImageView) findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idAlumnoSearch = txtIdAlumno.getText().toString().trim();
                if (!idAlumnoSearch.equals("") | !idAlumnoSearch.isEmpty()) {
                    Toast.makeText(activity_reporte.this, "BUSCANDO ALUMNO "+idAlumnoSearch, Toast.LENGTH_SHORT).show();
                    mAlumnoProvider.getUser(idAlumnoSearch).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {

                                AlumnoModelo a = snapshot.getValue(AlumnoModelo.class);
                                String padreId = a.getPadre_id();
                                mPadreProvider.getPadre(padreId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                             p = snapshot.getValue(PadreModelo.class);
                                            Toast.makeText(activity_reporte.this, "Encibtrado padre "+p.getId_firebase(), Toast.LENGTH_SHORT).show();
                                            System.out.println("------------------------------------");
                                            System.out.println("p "+p.toString());
                                            System.out.println("snapshot padre "+snapshot.toString());

                                            System.out.println("------------------------------------");
                                            mPadrePreferences =  getSharedPreferences("Padre",MODE_PRIVATE);
                                            SharedPreferences.Editor editor = mPadrePreferences.edit();
                                            editor.putString("padreId",p.getId_firebase());
                                            editor.apply();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(activity_reporte.this, "Error al obtener el id del padre", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(getApplicationContext(), "Introduzca una matricula valida", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(activity_reporte.this, "Error al obtener el id del padre", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                else {
                    Intent i = new Intent(getApplicationContext(), activity_escolaridad.class);
                    startActivity(i);
                }

            }
        });
        btnReportar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                if (infoReporte()) {
                    padreId = mPadrePreferences.getString("padreId","Vacio");
                    sendNotification(padreId);
                }

            }
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sendNotification(String padreId) {
        try {

            Toast.makeText(this, "PadreId Send "+padreId, Toast.LENGTH_SHORT).show();

            final String idAlumno = txtIdAlumno.getText().toString();
            final String urgencia = txtUrgencia.getText().toString();
            mTokenProvider.getToken(padreId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String idReporte = mGenerateId.getPassword(20);
                        reporteModelo.setAlumno_id(alumnoModelo.getId());
                        reporteModelo.setAsignatura_id(alumnoModelo.getGrupo_id());
                        reporteModelo.setGravedad(urgencia);
                        reporteModelo.setFecha(fechaActual);
                        reporteModelo.setTipo(tipoReporte);
                        reporteModelo.setDescripcion(descripcionReporte);
                        reporteModelo.setMaestro_id(mAuthProvider.getId());
                        reporteModelo.setStatus("Enviado");
                        reporteModelo.setIdReporte(idReporte);

                        String token = snapshot.child("token").getValue().toString();
                        Map<String, String> data = new HashMap<>();
                        data.put("title", "Alerta de reporte");
                        data.put("body", "Su hijo ha sido reportado ");
                        data.put("idAlumno", idAlumno);
                        data.put("idMaestro", mAuthProvider.getId());
                        data.put("descripcion", descripcionReporte);
                        data.put("idReporte", idReporte);
                        data.put("gravedad", reporteModelo.getGravedad());
                        data.put("fecha", fechaActual);
                        data.put("tipoReporte", tipoReporte);
                        data.put("idPadre",padreId);
                        System.out.println("-------------------------------------------");
                        System.out.println("IdAlumno a reoortar"+idAlumno);
                        System.out.println("-------------------------------------------");

                        FCMBody fcmBody = new FCMBody(token, "high", data);
                        mReporteProvider.create(reporteModelo);

                        mNotificationProvider.sendNotification(fcmBody).enqueue(new Callback<FCMResponse>() {
                            @Override
                            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                if (call.isExecuted()) {
                                    toastHelper.LanzarToast("Se envio la notificacion", 1, getApplicationContext(), Gravity.BOTTOM);
                                    Intent i = new Intent(getApplicationContext(),activity_PrincipalMaestro.class);
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onFailure(Call<FCMResponse> call, Throwable t) {
                                toastHelper.LanzarToast("No se pudo enviar la notificacion", 2, getApplicationContext(), Gravity.CENTER);
                            }
                        });
                    } else {
                        toastHelper.LanzarToast("No se encontro el Token", Toast.LENGTH_SHORT, getApplicationContext(), Gravity.BOTTOM);
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    toastHelper.LanzarToast("No se encontro el Token", Toast.LENGTH_SHORT, getApplicationContext(), Gravity.BOTTOM);


                }
            });
        } catch (Exception e) {
            System.out.println("Exepcion en sendNotifiction " + e);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            padreId = getIntent().getStringExtra("padre_ID");
            alumnoModelo = (AlumnoModelo) getIntent().getSerializableExtra("Alumno");


            txtIdAlumno.setText(alumnoModelo.getId());

            if (!alumnoModelo.getPadre_id().equals("")) {
                getInfoPadre(alumnoModelo);

            }


        } catch (Exception e) {
            Log.e("Error en el OnStart", e.toString());
        }
    }


    public void getInfoPadre(final AlumnoModelo a) {
        mPadreProvider.getPadre(a.getPadre_id()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    PadreModelo p = snapshot.getValue(PadreModelo.class);
                    assert p != null;
                    padreId = p.getId_firebase();
                    mPadrePreferences =  getSharedPreferences("Padre",MODE_PRIVATE);
                    SharedPreferences.Editor editor = mPadrePreferences.edit();
                    editor.putString("padreId",padreId);
                    editor.apply();
                } else {
                    System.out.println("-------------------");
                    System.out.println("No se encontro el padre " + a.getPadre_id());
                    System.out.println("-------------------");
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("------------------------");
                System.out.println("Error en consulta padre");
            }
        });

    }


    public boolean infoReporte() {
        String descripcion = txtDescripcion.getText().toString();
        if (descripcion.length() > 10) {
            descripcionReporte = txtDescripcion.getText().toString();
            if (rdbtnDiciplinario.isChecked()) {
                tipoReporte = "Reporte Diciplinario";
            } else if (rdbtnAcademico.isChecked()) {
                tipoReporte = "Reporte Academico";
            } else {
                toastHelper.LanzarToast("Desbes Seleccionar un tiṕo de reporte", Toast.LENGTH_SHORT, this, Gravity.BOTTOM);
                return false;
            }
        } else {
            toastHelper.LanzarToast("Se debe agregar  descripcion valida", Toast.LENGTH_SHORT, this, Gravity.BOTTOM);
            return false;
        }
        return true;
    }
}