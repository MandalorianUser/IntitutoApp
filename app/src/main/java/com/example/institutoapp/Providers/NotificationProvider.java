package com.example.institutoapp.Providers;

import com.example.institutoapp.Models.FCMBody;
import com.example.institutoapp.Models.FCMResponse;
import com.example.institutoapp.retrofit.IFCMApi;
import com.example.institutoapp.retrofit.RetrofitClient;

import retrofit2.Call;

public class NotificationProvider {
    private String url = "https://fcm.googleapis.com";

    public NotificationProvider() {
    }

    public Call<FCMResponse> sendNotification(FCMBody body){
        return RetrofitClient.getClientObject(url).create(IFCMApi.class).send(body);

    }
}
