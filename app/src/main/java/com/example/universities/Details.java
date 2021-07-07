package com.example.universities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

public class Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent;
        intent = getIntent();
        String country="";
        String name="";
        country+= intent.getStringExtra("country");
        name+=intent.getStringExtra("name");
        OkHttpClient client = new OkHttpClient();
        String url = "http://universities.hipolabs.com/search?name="+name+"&country="+country;
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();
        String finalCountry = country;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Details.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("YAYAYAYAY", myResponse);
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

        });
    }
}