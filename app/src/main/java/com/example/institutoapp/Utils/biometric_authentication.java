package com.example.institutoapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.an.biometric.BiometricCallback;
import com.an.biometric.BiometricManager;


public class biometric_authentication implements BiometricCallback {
    Activity mContext;
    Class claseDestino;

    public biometric_authentication(Activity context,Class clase) {

        mContext = context;
        claseDestino = clase;

    }

    public void huella(Context context) {
        System.out.println("--------------------------------------");
        System.out.println("Entro al metodo huella");
        System.out.println("--------------------------------------");
        new BiometricManager.BiometricBuilder(context)
                .setTitle("Verifique su identidad")
                .setSubtitle(" ")
                .setDescription("Para continuar, por favor identifiquese!")
                .setNegativeButtonText("Cancelar")
                .build()
                .authenticate((BiometricCallback) mContext);

    }

    @Override
    public void onSdkVersionNotSupported() {

    }

    @Override
    public void onBiometricAuthenticationNotSupported() {

    }

    @Override
    public void onBiometricAuthenticationNotAvailable() {
        System.out.println("--------------------------------------");
        System.out.println("huella not avaible");
        System.out.println("--------------------------------------");
        Toast.makeText(mContext, "Por favor  inicie sesion de nuevo", Toast.LENGTH_SHORT).show();
        mContext.finish();
    }

    @Override
    public void onBiometricAuthenticationPermissionNotGranted() {
        Toast.makeText(mContext, "Por favor  inicie sesion de nuevo", Toast.LENGTH_SHORT).show();
        mContext.finish();
    }

    @Override
    public void onBiometricAuthenticationInternalError(String error) {
        System.out.println("--------------------------------------");
        System.out.println(" huella internal erro");
        System.out.println("--------------------------------------");
        Toast.makeText(mContext, "Por favor  inicie sesion de nuevo", Toast.LENGTH_SHORT).show();
        mContext.finish();
    }

    @Override
    public void onAuthenticationFailed() {
        System.out.println("--------------------------------------");
        System.out.println(" huella Failed");
        System.out.println("--------------------------------------");
        Toast.makeText(mContext, "Por favor  inicie sesion de nuevo", Toast.LENGTH_SHORT).show();
        mContext.finish();
    }

    @Override
    public void onAuthenticationCancelled() {
        Toast.makeText(mContext, "Por favor  inicie sesion de nuevo", Toast.LENGTH_SHORT).show();
        mContext.finish();
    }

    @Override
    public void onAuthenticationSuccessful() {
        Toast.makeText(mContext, "Login Succesfull", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        Toast.makeText(mContext, "Por favor  inicie sesion de nuevo", Toast.LENGTH_SHORT).show();
        mContext.finish();
    }
}
