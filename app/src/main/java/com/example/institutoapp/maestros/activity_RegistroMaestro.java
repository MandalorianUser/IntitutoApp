package com.example.institutoapp.maestros;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.institutoapp.Models.MaestroModelo;
import com.example.institutoapp.Padres.activity_registro_padre;
import com.example.institutoapp.Providers.AuthProvider;
import com.example.institutoapp.Providers.ImageProvider;
import com.example.institutoapp.Providers.MaestroProvider;
import com.example.institutoapp.R;
import com.example.institutoapp.Utils.CloseKeyboard;
import com.example.institutoapp.Utils.FileUtil;
import com.example.institutoapp.Utils.InfoDialog;
import com.example.institutoapp.Utils.Network;
import com.example.institutoapp.Utils.ToastHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class activity_RegistroMaestro extends AppCompatActivity {

    private ImageView btnFoto, FotoPerfil;
    private CircleImageView btnAtras;
    private File mImageFile;
    private TextInputEditText edtId, edtNombre, edtPass1, edtPass2, edtEmail;
    private static final int GALLERY_INTENT = 1;
    private MaestroProvider mMaestroProvider;
    private AuthProvider authProvider;
    private ImageProvider mImageProvider;
    private Network mNetwork;
    private InfoDialog infoDialog;
    private ToastHelper mToasthelper;

    private MaestroModelo maestroModelo;
    private CloseKeyboard c;
    private int errores;
    private TextInputLayout txtEmail, txtPass1, txtPass2, txtId, txtNombre;
    private final int GALLERY_REQUEST_CODE = 1;
    private AlertDialog mDialog;
    private String urlImageUser;
    private Button btnRegistrarMaestro;
    private SharedPreferences sharedPreferences;
    private String maestroMatricula;
    private SharedPreferences mPref;

    private static final String MESSAGE_REGISTER_INFO = "Para poderse registar por favor introduzca su matricula institucional en el campo requerido";
    private static final String TITLE_INFO_REGISTER = "Matricula Requerida!";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_maestro);
        authProvider = new AuthProvider();
        mImageProvider = new ImageProvider();
        mMaestroProvider = new MaestroProvider();
        maestroModelo = new MaestroModelo();
        mNetwork = new Network();
        mToasthelper = new ToastHelper();
        infoDialog = new InfoDialog();
        mDialog = new SpotsDialog.Builder().setContext(activity_RegistroMaestro.this).setMessage("Espere un momento").build();

        btnRegistrarMaestro = findViewById(R.id.btnMaestroRegistro);
        txtId = findViewById(R.id.txtMaestroIdLayout);
        edtId = findViewById(R.id.txtMaestroId);
        FotoPerfil = findViewById(R.id.FotoPerfil);
        txtNombre = findViewById(R.id.txtMaestroNombreLayout);
        edtNombre = findViewById(R.id.txtMaestroNombre);
        edtPass1 = findViewById(R.id.txtRegistroMaestroPass1);
        edtPass2 = findViewById(R.id.txtRegistroMaestroPass2);
        txtEmail = findViewById(R.id.txtMaestroEmailLayout);
        txtPass1 = findViewById(R.id.txtRegistroMaestroPass1Layout);
        txtPass2 = findViewById(R.id.txtRegistroMaestroPass2Layout);
        edtEmail = findViewById(R.id.txtMaestroEmail);
        btnFoto = findViewById(R.id.btnSelecccionarImagen);
        btnAtras = findViewById(R.id.btnAtrasRegisterMaestro);

        errores = 0;


        btnRegistrarMaestro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    registerUser();
                } catch (Exception e) {
                    System.out.println("Error" + e);
                }

            }
        });
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

            }
        });
    }//Fin del OnCreate

    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }


    private int validar() {
        String nombre = edtNombre.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String Pass1 = edtPass1.getText().toString().trim();
        String Pass2 = edtPass2.getText().toString().trim();
        String id = edtId.getText().toString().trim();
        errores = 0;

        if (id.equals("")) {
            edtId.setError("Obligatorio");
            errores++;

        }
        if (email.equals("")) {
            edtEmail.setError("Obligatorio");
            errores++;
        }
        if (nombre.equals("")) {
            edtNombre.setError("Obligatorio");
            errores++;
        }


        if (Pass1.equals("")) {
            edtPass1.setError("Obligatorio");
            errores++;
        }
        if (Pass2.equals("")) {
            edtPass2.setError("Obligatorio");
            errores++;

        }
        if (Pass1 != "" & Pass2 != "") {
            Boolean coinciden = false;

            if (Pass1.equals(Pass2)) {
                if (Pass1.length() < 6) {
                    errores++;
                    edtPass2.setError("La contraseña debe tener almenos 6 caracteres");
                }

            } else {
                edtPass2.setError("Las contraseñas no coinciden");
                errores++;
            }

        }
        return errores;
    }

    private void Lanzar(String Email) {
        mDialog.dismiss();
        authProvider.logout();
        Toast.makeText(getApplicationContext(), "Registro Exitoso!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), activity_LoginMaestro.class);
        i.putExtra("Correo", Email);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }


    private void RegistrarUsuario() {
        final String email = edtEmail.getText().toString().trim();
        final String pass = edtPass2.getText().toString().trim();
        authProvider.register(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Login(email, pass);
                }
            }

            private void Login(final String email, String pass) {
                final String id_institucional = edtId.getText().toString().trim();
                final String nombre = edtNombre.getText().toString().trim();
                authProvider.login(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final String id_firebase = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            mImageProvider.save(activity_RegistroMaestro.this, mImageFile, id_firebase).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                urlImageUser = uri.toString();
                                                maestroModelo.setImagen(urlImageUser);
                                                maestroModelo.setEmail(email);
                                                maestroModelo.setId_firebase(FirebaseAuth.getInstance().getUid());
                                                maestroModelo.setNombre(nombre);
                                                maestroModelo.setId_institucional(id_institucional);
                                                databaseAlta(maestroModelo);
                                            }
                                        });

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void databaseAlta(final MaestroModelo m) {
        mMaestroProvider.create(m).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Lanzar(m.getEmail());
                }
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri;
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {

            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);


        }
        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    imageUri = result.getUri();
                    assert data != null;
                    mImageFile = FileUtil.from(this, imageUri);
                    FotoPerfil.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
                    btnFoto.setVisibility(View.INVISIBLE);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Toast.makeText(getApplicationContext(), "Error: " + error, Toast.LENGTH_LONG).show();
                    System.out.println("Error Crop:" + error);
                }
            }
        } catch (Exception e) {
            Log.d("Error", "Se produjo un error " + e);
            Toast.makeText(getApplicationContext(), "Se produjo un error " + e, Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        mPref =   getSharedPreferences("MatriculaVerificada",Context.MODE_PRIVATE);
        if (mNetwork.HasInternet(activity_RegistroMaestro.this)){
            String matriculaVerificada = mPref.getString("MatriculaVerificada","no");
            if (matriculaVerificada.equals("si")){
                showForms();
                registerUser();
            }else{
              verifyUser();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Sin internet, por favor revise su conexión", Toast.LENGTH_SHORT).show();
            hideForms();
            hasInternet();

        }

    }

    private void verifyUser() {
        hideForms();
        SharedPreferences.Editor editor = mPref.edit();
        infoDialog.showInfoDialow(TITLE_INFO_REGISTER,MESSAGE_REGISTER_INFO,activity_RegistroMaestro.this);
        txtId.setVisibility(View.VISIBLE);
        btnRegistrarMaestro.setText("Verificar");
        btnRegistrarMaestro.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btnRegistrarMaestro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maestroMatricula = edtId.getText().toString().trim();
                if(maestroMatricula.length()>8){
                    mMaestroProvider.verifyUser(maestroMatricula).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                Toast.makeText(getApplicationContext(), "Matricula Verificada", Toast.LENGTH_SHORT).show();
                                editor.putString("MatriculaVerificada","si");
                                editor.apply();
                                showForms();
                            registerUser();
                            }else
                                Toast.makeText(getApplicationContext(), "No existe la matricula", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "Error en la conexion", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else
                    Toast.makeText(getApplicationContext(), "Matricula invalida", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getSharedPreferences("MatriculaVerificada", MODE_PRIVATE).edit();
        editor.clear().apply();

    }

    public void hasInternet() {

        btnRegistrarMaestro.setText("Revisar conexión");
        btnRegistrarMaestro.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btnRegistrarMaestro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean hasInternet = mNetwork.HasInternet(activity_RegistroMaestro.this);
                if (hasInternet) {
                    mToasthelper.LanzarToast("En linea de nuevo !!", Toast.LENGTH_SHORT, getApplicationContext(), Gravity.CENTER_VERTICAL);
                    maestroMatricula = sharedPreferences.getString("MatriculaVerificada","no");
                    if (maestroMatricula.equals("si")) {
                        registerUser();
                    } else {
                        verifyUser();
                    }
                } else
                    mToasthelper.LanzarToast("Sin conexion aun!!", Toast.LENGTH_SHORT, getApplicationContext(), Gravity.CENTER_VERTICAL);
            }
        });
    }


    public void showForms() {
        txtId.setVisibility(View.VISIBLE);
        txtEmail.setVisibility(View.VISIBLE);
        txtPass1.setVisibility(View.VISIBLE);
        txtPass2.setVisibility(View.VISIBLE);
        edtEmail.setVisibility(View.VISIBLE);
        txtNombre.setVisibility(View.VISIBLE);
        btnFoto.setVisibility(View.VISIBLE);
        FotoPerfil.setVisibility(View.VISIBLE);
        btnRegistrarMaestro.setText(R.string.btnRegistrar);
    }

    public void hideForms() {
        txtId.setVisibility(View.INVISIBLE);
        txtEmail.setVisibility(View.INVISIBLE);
        txtPass1.setVisibility(View.INVISIBLE);
        txtPass2.setVisibility(View.INVISIBLE);
        txtNombre.setVisibility(View.INVISIBLE);
        btnFoto.setVisibility(View.INVISIBLE);
        FotoPerfil.setVisibility(View.INVISIBLE);

    }

    private void registerUser() {
        btnRegistrarMaestro.setText("Registrar");
        btnRegistrarMaestro.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btnRegistrarMaestro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.CloseKeyboard(activity_RegistroMaestro.this);
                errores = validar();
                if (errores == 0) {
                    mDialog.show();
                    RegistrarUsuario();
                }
            }
        });

    }


}





