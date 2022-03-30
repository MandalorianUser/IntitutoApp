package com.example.institutoapp.Utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastHelper {
    public void LanzarToast(String Mensaje, int duracion, Context context, int gravity){

        Toast toast= Toast.makeText(context, Mensaje, duracion);
        toast.setGravity(gravity, 0, 0); toast.show();


    }
}
