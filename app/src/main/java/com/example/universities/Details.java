package com.example.universities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class Details extends AppCompatActivity {
    int cacheSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Log.i("PPPPPPPP", "ppp");
        TextView textViewName = (TextView) findViewById(R.id.name);
        TextView textViewCountry = (TextView) findViewById(R.id.country);
        TextView textViewDomain = (TextView) findViewById(R.id.domain);
        TextView textViewWebsite = (TextView) findViewById(R.id.website);
        TextView textViewCode = (TextView) findViewById(R.id.code);
        SQLiteDatabase myDB = this.openOrCreateDatabase("details", MODE_PRIVATE, null);
        Intent intent;
        intent = getIntent();
        String country = "";
        String name = "";
        country += intent.getStringExtra("country");
        name += intent.getStringExtra("name");
        OkHttpClient client = new OkHttpClient();
        String url = "http://universities.hipolabs.com/search?name=" + name + "&country=" + country;
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();
        String finalCountry = country;
        String finalName = name;
        String finalCountry1 = country;
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
        Cursor resultSet = myDB.rawQuery("Select domains from detail_uni  where name='Yashvi'", null);
        resultSet.moveToFirst();
        if (!connected) {
            if (resultSet.getCount() == 0) {
                //print no data available
            } else {
                //show data
            }
        } else{
            String finalName1 = name;
            String finalCountry2 = country;
            client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Details.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("YAYAYAYAY", myResponse);
                            int i = 15;
                            String domains = "", alpha_two_code = "", state_province = "null";
                            String web_pages="";
                            while (!myResponse.substring(i, i + 1).equals("\"")) {
                                domains += myResponse.substring(i, i + 1);
                                i++;
                            }
                            while (i < myResponse.length()) {
                                if (i + 9 < myResponse.length() && myResponse.substring(i, i + 9).equals("web_pages")) {
                                    i = i + 14;
                                    while (i + 1 < myResponse.length() && !myResponse.substring(i, i + 1).equals("\"")) {
                                        web_pages+= myResponse.substring(i, i + 1);
                                        i++;
                                    }
                                    i--;
                                    break;
                                }
                                i++;
                            }
                            while (i < myResponse.length()) {
                                if (i + 14 < myResponse.length() && myResponse.substring(i, i + 14).equals("alpha_two_code")) {
                                    i = i + 18;
                                    while (i + 1 < myResponse.length() && !myResponse.substring(i, i + 1).equals("\"")) {
                                        alpha_two_code += myResponse.substring(i, i + 1);
                                        i++;
                                    }
                                    i--;
                                    break;
                                }
                                i++;
                            }
                            Log.i("domains", domains);
                            Log.i("name", finalName);
                            Log.i("alpha 2 code", alpha_two_code);
                            Log.i("country", finalCountry1);
                            //show info
                            textViewName.setText(finalName1);
                            textViewCountry.setText(finalCountry2);
                            textViewDomain.setText(domains);
                            textViewWebsite.setText(web_pages);
                            textViewCode.setText(alpha_two_code);
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
}