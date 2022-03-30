package com.example.institutoapp.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.example.institutoapp.Models.ReporteModelo;
import com.example.institutoapp.R;
import com.example.institutoapp.activity_detail_reporte;
import com.example.institutoapp.activity_padre_notification_entered;
import com.example.institutoapp.channel.NotificationHelper;
import com.example.institutoapp.receivers.DetailReceiver;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.Serializable;
import java.util.Map;

public class MyFirebaseMessagingClient extends FirebaseMessagingService {
    private static final int NOTIFICATION_CODE = 100;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        ReporteModelo reporteModelo = new ReporteModelo();
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String body = data.get("body");
        String idAlumno = data.get("idAlumno");
        reporteModelo.setAlumno_id(idAlumno);
        reporteModelo.setDescripcion(data.get("body"));
        reporteModelo.setGravedad(data.get("gravedad"));
        reporteModelo.setAlumno_id(data.get("idAlumno"));
        reporteModelo.setMaestro_id(data.get("idMaestro"));
        reporteModelo.setIdReporte(data.get("idReporte"));
        reporteModelo.setFecha(data.get("fecha"));
        reporteModelo.setDescripcion(data.get("descripcion"));
        reporteModelo.setTipo(data.get("tipoReporte"));
        if (title != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if(title.contains("Alerta de reporte")){
                        showNotificationOreo(title, body,reporteModelo);
                        showNotificationOreoActivity(title, reporteModelo, body);
                    }
                    else{
                        showNotificationOreoMaestro(title, body,reporteModelo);
                        showNotificationOreoActivityMaestro(title, reporteModelo, body);
                    }



            } else {
                if (title.contains("Alerta de reporte")){
                    showNotification(title, body,reporteModelo);
                    showNotificationOreoActivity(title, reporteModelo, body);
                }
                else{
                    showNotificationMaestro(title,body,reporteModelo);
                    showNotificationActivityMaestro(title,body,reporteModelo);
                }


            }
        }

    }

    private void showNotificationOreoActivityMaestro(String title, ReporteModelo reporteModelo, String body) {
        PowerManager pm = (PowerManager) getBaseContext().getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if(!isScreenOn){
            PowerManager.WakeLock wakeLock = pm.newWakeLock(
                    PowerManager.FULL_WAKE_LOCK|
                            PowerManager.ACQUIRE_CAUSES_WAKEUP|
                            PowerManager.ON_AFTER_RELEASE,
                    "AppName:MyLock"

            );
            wakeLock.acquire(100000);
        }
        Intent intent = new Intent(getBaseContext(),activity_padre_notification_entered.class);
        intent.putExtra("Reporte",reporteModelo);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotificationOreoMaestro(String title, String body, ReporteModelo reporteModelo) {
        Intent intentReporte = new Intent(getApplicationContext(), activity_padre_notification_entered.class);
        intentReporte.putExtra("Reporte",reporteModelo);

        PendingIntent intent = PendingIntent.getActivity(getBaseContext(), 0, intentReporte, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        Notification.Builder builder = notificationHelper.getNotification(title, body, intent, sound);
        notificationHelper.getManager().notify(1, builder.build());
    }

    private void showNotificationOreoActivity(String title, ReporteModelo reporteModelo, String body) {
        PowerManager pm = (PowerManager) getBaseContext().getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if(!isScreenOn){
            PowerManager.WakeLock wakeLock = pm.newWakeLock(
                    PowerManager.FULL_WAKE_LOCK|
                                PowerManager.ACQUIRE_CAUSES_WAKEUP|
                                PowerManager.ON_AFTER_RELEASE,
                                "AppName:MyLock"

            );
            wakeLock.acquire(100000);
        }
        Intent intent = new Intent(getBaseContext(),activity_detail_reporte.class);
        intent.putExtra("Reporte",reporteModelo);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotificationOreoActions(String title, ReporteModelo reporteModelo, String body) {
        Bundle bundle = new Bundle();
        Intent detailIntent = new Intent(this, DetailReceiver.class);
        PendingIntent detailPendingIntent = PendingIntent.getBroadcast(this,
                NOTIFICATION_CODE, detailIntent, PendingIntent.FLAG_ONE_SHOT);
        bundle.putString("body",body);
        detailIntent.putExtras(bundle);
        Notification.Action detailAction = new Notification.Action.Builder(
                R.mipmap.ic_launcher,
                "Detalles",
                detailPendingIntent
        ).build();
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        Notification.Builder builder = notificationHelper.getNotificationActions(title, reporteModelo.getDescripcion(), sound, detailAction);
        notificationHelper.getManager().notify(2, builder.build());
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private void showNotificationActions(String title, ReporteModelo reporteModelo, String body) {
        Intent detailIntent = new Intent(this, DetailReceiver.class);
        detailIntent.putExtra("body", body);
        detailIntent.putExtra("title", title);
        PendingIntent detailPendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_CODE, detailIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action detailAction = new NotificationCompat.Action.Builder(
                R.mipmap.ic_launcher,
                "Detalles",
                detailPendingIntent
        ).build();


        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = notificationHelper.getNotificationActionsOldApi(title, reporteModelo.getDescripcion(), sound, detailAction);
        notificationHelper.getManager().notify(2, builder.build());
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotificationOreo(String title, String body,ReporteModelo reporteModelo ) {
        Intent intentReporte = new Intent(getApplicationContext(),activity_detail_reporte.class);
        intentReporte.putExtra("Reporte",reporteModelo);

        PendingIntent intent = PendingIntent.getActivity(getBaseContext(), 0, intentReporte, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        Notification.Builder builder = notificationHelper.getNotification(title, body, intent, sound);
        notificationHelper.getManager().notify(1, builder.build());
    }

    private void showNotification(String title, String body, ReporteModelo reporteModelo) {
        Intent intentReporte = new Intent(getApplicationContext(),activity_detail_reporte.class);
        intentReporte.putExtra("Reporte",reporteModelo);

        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0,intentReporte , PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = notificationHelper.getNotificationOldApi(title, body, pendingIntent, sound);
        notificationHelper.getManager().notify(1, builder.build());
    }


    private void showNotificationMaestro(String title, String body, ReporteModelo reporteModelo) {
        Intent intentReporte = new Intent(getApplicationContext(),activity_padre_notification_entered.class);
        intentReporte.putExtra("Reporte",reporteModelo);

        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0,intentReporte , PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = notificationHelper.getNotificationOldApi(title, body, pendingIntent, sound);
        notificationHelper.getManager().notify(1, builder.build());
    }


    private void  showNotificationActivityMaestro(String title, String body, ReporteModelo reporteModelo ){
        PowerManager pm = (PowerManager)getBaseContext().getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if (!isScreenOn){
            PowerManager.WakeLock wakeLock = pm.newWakeLock(
                    PowerManager.FULL_WAKE_LOCK |
                                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                                PowerManager.ON_AFTER_RELEASE,
                                "AppName:MyLock"
            );
            wakeLock.acquire(10000);
        }
        Intent i = new Intent(getBaseContext(),activity_padre_notification_entered.class);
        i.putExtra("Reporte",reporteModelo);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
