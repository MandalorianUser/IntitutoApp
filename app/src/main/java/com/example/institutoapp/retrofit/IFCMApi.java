package com.example.institutoapp.retrofit;


import com.example.institutoapp.Models.FCMBody;
import com.example.institutoapp.Models.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAGl4qnbs:APA91bGno1jghJQAtRUxD3ccno7wuhLZdts0ve62B1LfV58fZEclyFsrhk6wE2wmbPZXeMY0RlgTx6W5X9NETnbwKyP_ayyZo2nvstr6v0Zh4xLMLj0H4F2akUjyqX-Tb1WlczbS6exO"

    })
    @POST("fcm/send")
    Call<FCMResponse> send(@Body FCMBody body);


}
