package com.example.institutoapp.Padres;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.institutoapp.Models.MatriculaModelo;
import com.example.institutoapp.Models.PadreModelo;
import com.example.institutoapp.Providers.AuthProvider;
import com.example.institutoapp.Providers.ImageProvider;
import com.example.institutoapp.Providers.PadreProvider;
import com.example.institutoapp.R;
import com.example.institutoapp.Utils.CloseKeyboard;
import com.example.institutoapp.Utils.FileUtil;
import com.example.institutoapp.Utils.InfoDialog;
import com.example.institutoapp.Utils.Network;
import com.example.institutoapp.Utils.ToastHelper;
import com.example.institutoapp.activity_bienvenida;
import com.example.institutoapp.maestros.activity_RegistroMaestro;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class activity_registro_padre extends AppCompatActivity {
    private static final String MESSAGE_REGISTER_INFO = "Para poderse registar por favor introduzca su matricula institucional en el campo requerido";
    private static final String TITLE_INFO_REGISTER = "Matricula Requerida!";
    private static final int REQUEST_CODE = 200;


    private TextInputEditText edtIdPadre, edtEmailPadre, edtNombrePadre, edtPass1Padre, edtPass2Padre;
    private TextInputLayout txtPass1PadreLayout, txtPass2PadreLayout, txtIdPadreLayout, edtEmailPadreLayout, edtNombrePadreLayout;
    ;
    private ImageProvider mImageProvider;
    private AuthProvider mAuthProvider;
    private PadreProvider mPadreProvider;
    private ToastHelper mToastHelper;
    private InfoDialog infoDialog;
    private Network mNetwork;
    private CloseKeyboard closeKeyboard;
    private final int GALLERY_REQUEST_CODE = 1;
    private Uri imageUri;
    private File mImageFile;
    private String urlImageUser;
    private PadreModelo padreModelo;
    private Button btnRegristarPadre;
    private CircleImageView btnSeleccionarImagen;
    private CircleImageView fotoPerfil;
    private SharedPreferences preferencias;
    AlertDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_padre);
        mPadreProvider = new PadreProvider();
        mAuthProvider = new AuthProvider();
        mImageProvider = new ImageProvider();
        padreModelo = new PadreModelo();
        mToastHelper = new ToastHelper();
        closeKeyboard = new CloseKeyboard();
        infoDialog = new InfoDialog();
        mNetwork = new Network();


        btnRegristarPadre = findViewById(R.id.btnRegistroRegistrarPadre);
        btnSeleccionarImagen = findViewById(R.id.btnPadreSelecccionarImagen);
        edtIdPadre = findViewById(R.id.txtIdPadre);
        fotoPerfil = findViewById(R.id.FotoPerfilPadre);
        edtEmailPadre = findViewById(R.id.txtEmailPadre);
        edtNombrePadre = findViewById(R.id.txtNombrePadre);
        edtPass1Padre = findViewById(R.id.txtPass1Padre);
        edtPass2Padre = findViewById(R.id.txtPass2Padre);
        CircleImageView btnAtras = findViewById(R.id.btnAtrasRegisterPadre);
        edtNombrePadreLayout = findViewById(R.id.txtNombrePadreLayout);
        txtPass1PadreLayout = findViewById(R.id.txtPass1PadreLayout);
        txtPass2PadreLayout = findViewById(R.id.txtPass2PadreLayout);
        edtEmailPadreLayout = findViewById(R.id.txtEmailPadreLayout);
        txtIdPadreLayout = findViewById(R.id.txtIdPadreLayout);
        mDialog = new SpotsDialog.Builder().setContext(activity_registro_padre.this).setMessage("Espere un momento").build();
        btnRegristarPadre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        btnSeleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestPermission()) {
                    openGallery();
                }

            }
        });
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity_registro_padre.this, activity_LoginPadre.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });


    }

    private void registerUser() {
        closeKeyboard.CloseKeyboard(activity_registro_padre.this);
        btnRegristarPadre.setText("Registrar");
        btnRegristarPadre.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btnRegristarPadre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int errores = 0;
                errores = validar();
                if (errores == 0) {


                    mDialog.show();
                    RegistrarUsuario();
                } else {
                    Toast.makeText(getApplicationContext(), "Por favor rellene todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }


    private int validar() {
        String nombre = edtNombrePadre.getText().toString().trim();
        String email = edtEmailPadre.getText().toString().trim();
        String Pass1 = edtPass1Padre.getText().toString().trim();
        String Pass2 = edtPass2Padre.getText().toString().trim();
        String id = edtIdPadre.getText().toString().trim();


        int errores = 0;
        if (imageUri == null || imageUri.equals("")) {
            Toast.makeText(this, "Debe seleccionar una imagen", Toast.LENGTH_SHORT).show();
            return 1;
        }
        if (id.equals("")) {
            edtIdPadre.setError("Obligatorio");
            errores++;

        }
        if (email.equals("")) {
            edtEmailPadre.setError("Obligatorio");
            errores++;
        }
        if (nombre.equals("")) {
            edtNombrePadre.setError("Obligatorio");
            errores++;
        }
        if (mImageFile == null || mImageFile.toString() == "") {
            errores++;
            Toast.makeText(this, "Se debe añadir una imagen", Toast.LENGTH_SHORT).show();
        }


        if (Pass1.equals("")) {
            edtPass1Padre.setError("Obligatorio");
            errores++;
        }
        if (Pass2.equals("")) {
            edtPass2Padre.setError("Obligatorio");
            errores++;

        }
        if (!Pass1.equals("") & !Pass2.equals("")) {
            if (!Pass1.equals(Pass2)) {
                edtPass2Padre.setError("Las contraseñas no coinciden");
                errores++;
            }
        }
        return errores;
    }


    private void RegistrarUsuario() {
        //Obtener Email y contraseña de los edit text
        final String Email = edtEmailPadre.getText().toString().trim();
        final String Pass = edtPass2Padre.getText().toString().trim();
        mAuthProvider.register(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loginPadre(Email, Pass);
                } else {
                    mDialog.dismiss();
                }
            }
        });
    }


    private void loginPadre(String Email, String Pass) {

        mAuthProvider.login(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final String id_firebase = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String nombre = edtNombrePadre.getText().toString().trim();
                    String id_institucional = edtIdPadre.getText().toString().trim();
                    mImageProvider.save(activity_registro_padre.this, mImageFile, id_firebase).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        urlImageUser = uri.toString();
                                        padreModelo.setImagen(urlImageUser);
                                        padreModelo.setEmail(Email);
                                        padreModelo.setId_firebase(FirebaseAuth.getInstance().getUid());
                                        padreModelo.setNombre(nombre);
                                        padreModelo.setId_institucional(id_institucional);
                                        createPadre(padreModelo);
                                    }
                                });

                            } else {
                                Toast.makeText(getApplicationContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                                mDialog.dismiss();
                            }
                        }
                    });
                }
            }
        });


    }

    public void createPadre(PadreModelo padreModelo) {
        mPadreProvider.create(padreModelo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(activity_registro_padre.this, "Se registro exitosamente", Toast.LENGTH_SHORT).show();
                    lanzar(padreModelo);
                } else {
                    mDialog.dismiss();
                }
            }
        });
    }


    private void lanzar(PadreModelo p) {
        mDialog.dismiss();
        Intent i = new Intent(getApplicationContext(), activity_LoginPadre.class);
        i.putExtra("id", p.getId());
        i.putExtra("email", p.getEmail());
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {

            assert data != null;
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
                    fotoPerfil.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
                    btnSeleccionarImagen.setVisibility(View.INVISIBLE);
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
        preferencias = getSharedPreferences("MatriculaVerificada", Context.MODE_PRIVATE);
        if (mNetwork.HasInternet(activity_registro_padre.this)) {
            String matriculaVerificada = preferencias.getString("MatriculaVerificada", "no");
            if (matriculaVerificada.equals("si")) {
                btnRegristarPadre.setText("Registrarse");
                btnRegristarPadre.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                btnRegristarPadre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showForm();
                        registerUser();
                    }
                });
            } else {Log.e("MatriculaOnStart","No se encontro"+matriculaVerificada);
                hideForm();
                verifyUser();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Sin internet, por favor revise su conexión", Toast.LENGTH_SHORT).show();
            hideForm();
            hasInternet();

        }

    }

    private void verifyUser() {
        SharedPreferences.Editor editor = preferencias.edit();
        infoDialog.showInfoDialow(TITLE_INFO_REGISTER,MESSAGE_REGISTER_INFO, activity_registro_padre.this);
        txtIdPadreLayout.setVisibility(View.VISIBLE);
        edtIdPadre.setVisibility(View.VISIBLE);
        btnRegristarPadre.setText("Verificar");
        btnRegristarPadre.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btnRegristarPadre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String padreMatricula = edtIdPadre.getText().toString().trim();
                if(padreMatricula.length()>8){
                    mPadreProvider.verifyUser(padreMatricula).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                Toast.makeText(getApplicationContext(), "Matricula Verificada", Toast.LENGTH_SHORT).show();
                                editor.putString("MatriculaVerificada","si");
                                editor.apply();
                                showForm();
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
        Log.i("OnDestroy","Ejecutado");
        super.onDestroy();
        SharedPreferences.Editor editor = getSharedPreferences("MatriculaVerificada", MODE_PRIVATE).edit();
        editor.clear().apply();

    }

    public void hasInternet() {
        btnRegristarPadre.setText("Revisar conexón");
        btnRegristarPadre.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btnRegristarPadre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean hasInternet = mNetwork.HasInternet(activity_registro_padre.this);
                if (hasInternet) {
                    mToastHelper.LanzarToast("En linea de nuevo !!", Toast.LENGTH_SHORT, getApplicationContext(), Gravity.CENTER_VERTICAL);
                    preferencias = getSharedPreferences("MatriculaVerificada", Context.MODE_PRIVATE);
                    String matriculaVerificada = preferencias.getString("MatriculaVerificada", "no");
                    if (matriculaVerificada.equals("si")) {
                        System.out.println("----------------------------------------");
                        System.out.println("MAtricula ya verificada mostrando los forms y ejecutando registerUser");
                        System.out.println("----------------------------------------");
                        showForm();
                        registerUser();
                    } else {
                        System.out.println("----------------------------------------");
                        System.out.println("MAtricula sin verificar ocultando los forms y ejecutando verifyuser");
                        System.out.println("----------------------------------------");
                        hideForm();
                        verifyUser();
                    }
                } else {
                    mToastHelper.LanzarToast("Sin conexion aun!!", Toast.LENGTH_SHORT, getApplicationContext(), Gravity.CENTER_VERTICAL);
                }

            }
        });
    }

    public void hideForm() {
        txtIdPadreLayout.setVisibility(View.INVISIBLE);
        btnSeleccionarImagen.setVisibility(View.INVISIBLE);
        fotoPerfil.setVisibility(View.INVISIBLE);
        edtIdPadre.setVisibility(View.INVISIBLE);
        edtEmailPadreLayout.setVisibility(View.INVISIBLE);
        edtNombrePadreLayout.setVisibility(View.INVISIBLE);
        txtPass1PadreLayout.setVisibility(View.INVISIBLE);
        txtPass2PadreLayout.setVisibility(View.INVISIBLE);
        btnRegristarPadre.setGravity(Gravity.CENTER_VERTICAL);


    }

    public void showForm() {
        txtIdPadreLayout.setVisibility(View.VISIBLE);
        btnSeleccionarImagen.setVisibility(View.VISIBLE);
        edtEmailPadreLayout.setVisibility(View.VISIBLE);
        edtNombrePadreLayout.setVisibility(View.VISIBLE);
        fotoPerfil.setVisibility(View.VISIBLE);
        edtIdPadre.setVisibility(View.VISIBLE);
        edtEmailPadre.setVisibility(View.VISIBLE);
        edtNombrePadre.setVisibility(View.VISIBLE);
        edtPass1Padre.setVisibility(View.VISIBLE);
        edtPass2Padre.setVisibility(View.VISIBLE);
        txtPass1PadreLayout.setVisibility(View.VISIBLE);
        txtPass2PadreLayout.setVisibility(View.VISIBLE);
    }


    private Boolean requestPermission() {
        if (ContextCompat.checkSelfPermission(activity_registro_padre.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.i("TAG", "Permission Granted");
            return true;
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity_registro_padre.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Log.i("TAG", "The user previusly rejected the permission");
            } else {
                Log.i("TAG", "Request Permission");
            }
            ActivityCompat.requestPermissions(activity_registro_padre.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("TAG", "Permission granted(request)");
                openGallery();

            } else {
                Log.i("TAG", "Permission denied(request)");
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity_registro_padre.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    new AlertDialog.Builder(this).setMessage("Se necesitan habilitar los permisos para usar esta eplicación")
                            .setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(activity_registro_padre.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);

                                }
                            })
                            .setNegativeButton("No gracias", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("TAG", "Permission denied again");
                                }
                            }).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Necesitas habilitar los permisos de manera manual", Toast.LENGTH_SHORT).show();
                }

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}


