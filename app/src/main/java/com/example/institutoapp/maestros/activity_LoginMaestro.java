package com.example.institutoapp.maestros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.an.biometric.BiometricCallback;
import com.an.biometric.BiometricManager;
import com.example.institutoapp.Providers.AuthProvider;
import com.example.institutoapp.Providers.MaestroProvider;
import com.example.institutoapp.R;
import com.example.institutoapp.Utils.CloseKeyboard;
import com.example.institutoapp.Utils.Network;
import com.example.institutoapp.Utils.ToastHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.example.institutoapp.Utils.biometric_authentication;

import dmax.dialog.SpotsDialog;

public class activity_LoginMaestro extends AppCompatActivity   {
    private ToastHelper mToasthelper;
    private biometric_authentication mBiometric;
    private CloseKeyboard mCloseKeyboard;
    private Network mNetwork;
    private Button btnRegistrar;
    private Button btnLogin;
    public AlertDialog mDialog;
    private AuthProvider mAuth;
    boolean verifyTextInput,mLoginUser;
    private EditText edtPass;
    private String mEmailRegistrado;
    private TextInputEditText edtEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_maestro);
        mToasthelper = new ToastHelper();
        mNetwork =  new Network();
        mAuth = new AuthProvider();
        mBiometric =  new biometric_authentication(activity_LoginMaestro.this,activity_PrincipalMaestro.class);
        mCloseKeyboard =  new CloseKeyboard();
        mDialog = new SpotsDialog.Builder().setContext(activity_LoginMaestro.this).setMessage("Espere un momento").build();
        btnRegistrar = (Button) findViewById(R.id.btnRegristarseMaestro);
        btnLogin = (Button) findViewById(R.id.btnIniciarSesionMaestro);
        edtEmail = (TextInputEditText) findViewById(R.id.txtLoginMaestroEmail);
        edtPass = (EditText) findViewById(R.id.txtPassLoginMaestro);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), activity_RegistroMaestro.class);
                startActivity(i);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNetwork.HasInternet(getApplicationContext()))
                Login();
                else
                    mToasthelper.LanzarToast("Parece que no hay internet, por favor revise su conexion"
                            ,Toast.LENGTH_LONG, getApplicationContext(), Gravity.CENTER_VERTICAL);
            }
        });



    }


    private void Login() {
        //Obtener Datos Para el login
        String Email = edtEmail.getText().toString().trim();
        String Pass = edtPass.getText().toString().trim();
        verifyTextInput = VerifyTxtInputs(Email, Pass);

        //Logeando usuario
        try {

            if (verifyTextInput) {
                mDialog.show();
                mAuth.login(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            mDialog.dismiss();
                            Intent i = new Intent(getApplicationContext(), activity_PrincipalMaestro.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Usuario o contraseña Incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    }





                });
            }
        } catch (Exception e) {
            mDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Error:" + e, Toast.LENGTH_LONG).show();
        }


    }

    private boolean VerifyTxtInputs(String Email, String Pass) {
        //Verificamos que las  cajas de texto no esten vacias
        if(Email!=null){
            if (TextUtils.isEmpty(Email)) {
                edtEmail.setError("Obligatorio");

                return false;
            }
        }

        if (TextUtils.isEmpty(Pass)) {
            edtPass.setError("Obligatorio");

            return false;
        }
        return true;

    }


    @Override
    protected void onStart() {
        super.onStart();
        //Recibimos los extras de la activity de Registro de Maestro
        try {
            mEmailRegistrado = getIntent().getStringExtra("Correo");
            edtEmail.setText(mEmailRegistrado);
            if(FirebaseAuth.getInstance().getUid()!=null){
                Toast.makeText(this, "Iniciando biometric", Toast.LENGTH_SHORT).show();
              mBiometric.huella(getApplicationContext());
            }
        }catch (Exception e){
            System.out.println("ERROR en el OnStart"+e);
        }


    }

}