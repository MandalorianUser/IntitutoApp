package com.example.institutoapp.Padres;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.an.biometric.BiometricCallback;
import com.an.biometric.BiometricManager;
import com.example.institutoapp.Providers.AuthProvider;
import com.example.institutoapp.R;
import com.example.institutoapp.Utils.CloseKeyboard;
import com.example.institutoapp.Utils.Network;
import com.example.institutoapp.Utils.ToastHelper;
import com.example.institutoapp.maestros.activity_LoginMaestro;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.example.institutoapp.Utils.biometric_authentication;

import dmax.dialog.SpotsDialog;
public class activity_LoginPadre extends AppCompatActivity {
    private ToastHelper mToasthelper;
    private CloseKeyboard mCloseKeyboard;
    private Network mNetwork;
    private Button btnLoginPadre, btnRegistrarPadre;
    private EditText txtEmail, txtContraseña;
    private AlertDialog mDialog;
    private AuthProvider mAuthProvider;
    private biometric_authentication mBiometric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_padre);
        mAuthProvider = new AuthProvider();
        mNetwork =  new Network();
        mToasthelper =  new ToastHelper();
        mBiometric = new biometric_authentication(activity_LoginPadre.this,activity_principal_padre.class);
        mCloseKeyboard =  new CloseKeyboard();
        try {
            String emailExtra = getIntent().getStringExtra("email");
            txtEmail.setText(emailExtra);
        }catch (Exception e){
            System.out.println("Oncreate"+e);
        }

        mDialog = new SpotsDialog.Builder().setContext(activity_LoginPadre.this).setMessage("Espere un momento").build();
        btnLoginPadre = findViewById(R.id.btnLoginIniciarSesionPadre);
        txtEmail = findViewById(R.id.txtLoginPadreEmail);
        txtContraseña = findViewById(R.id.txtPassLoginPadre);
        btnRegistrarPadre = findViewById(R.id.btnLoginRegistrarPadre);
        btnRegistrarPadre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity_LoginPadre.this, activity_registro_padre.class);
                startActivity(i);
            }
        });

        btnLoginPadre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(mNetwork.HasInternet(getApplicationContext())) {
                  if (validarCampos()) {
                      loginPadre();
                  } else {
                      Toast.makeText(getApplicationContext(), "Porfavor Rellene los campos", Toast.LENGTH_SHORT).show();
                  }
              } else
                  mToasthelper.LanzarToast("Parece que no hay internet, por favor revise su conexión",
                          Toast.LENGTH_SHORT,getApplicationContext(), Gravity.CENTER_VERTICAL);
            }
        });



    }

    private boolean validarCampos() {
        String email = txtEmail.getText().toString().trim();
        String pass  = txtContraseña.getText().toString().trim();
        return !email.equals("") && !email.isEmpty() && !pass.equals("") && !pass.isEmpty();
    }

    private  void loginPadre(){
        mDialog.show();
        String email = txtEmail.getText().toString().trim();
        String pass = txtContraseña.getText().toString().trim();
        mAuthProvider.login(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mDialog.dismiss();
                    Intent i = new Intent(activity_LoginPadre.this, activity_principal_padre.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
                else {
                    mDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Usuario o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        try {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            if (mAuth.getCurrentUser() != null) {
              mBiometric.huella(activity_LoginPadre.this);
                }

        } catch (Exception e) {
            Log.e("Error OnStartLoginPadre", "Error" + e);
        }

    }



    public void lanzar(){
        Intent i = new Intent(activity_LoginPadre.this, activity_principal_padre.class);
        startActivity(i);
        finish();
    }
}




