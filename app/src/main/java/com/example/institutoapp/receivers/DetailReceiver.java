package com.example.institutoapp.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.institutoapp.Providers.ReporteProvider;
import com.example.institutoapp.activity_detail_reporte;


public class DetailReceiver extends BroadcastReceiver {
    private ReporteProvider mReporteProvider;
    private Bundle bundle = new Bundle();

    @Override
    public void onReceive(Context context, Intent intent) {

        mReporteProvider = new ReporteProvider();
        mReporteProvider.updateStatus("1", "leido");
        Intent i = new Intent(context.getApplicationContext(),activity_detail_reporte.class);
        context.startActivity(i);



        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);


    }
}
