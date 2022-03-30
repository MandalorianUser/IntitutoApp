package com.example.institutoapp.Utils;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;

import com.example.institutoapp.maestros.activity_RegistroMaestro;

public class InfoDialog   {
    public void showInfoDialow(String title,String message,Context context) {
        final androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        androidx.appcompat.app.AlertDialog alert = dialog.create();
        alert.show();
    }
}
