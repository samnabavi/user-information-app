package com.example.userinformation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuickActivity extends AppCompatActivity {

    String myResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick);
        OkHttpClient client = new OkHttpClient();
        String url = "https://jsonplaceholder.typicode.com/users";
        //String url = "https://reqres.in/api/users?page=2";
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).

                enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        System.out.println("************************************************************");

                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws
                            IOException {
                        if (response.isSuccessful()) {

                            myResponse = response.body().string();
                            Intent intent = new Intent(QuickActivity.this, UsersActivity.class);
                            intent.putExtra("res", myResponse);
                            startActivity(intent);


                        }
                    }
                });
    }
}