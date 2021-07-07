package com.example.universities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

public class Search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent;
        intent = getIntent();
        String country="";
        country+= intent.getStringExtra("country");

        ArrayList<String> countries = new ArrayList<String>();

        OkHttpClient client = new OkHttpClient();
        String url = "http://universities.hipolabs.com/search?country="+country;
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();
        String finalCountry = country;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Search.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int i=0;
                            while(i<myResponse.length() && countries.size()<1001 ) {
                                if (i+4<myResponse.length() && myResponse.substring(i,i+4).equals("name")){
                                    i=i+8;
                                    String s="";
                                    while(i+1<myResponse.length() && !myResponse.substring(i,i+1).equals("\"")){
                                        s+=myResponse.substring(i,i+1);
                                        i++;
                                    }
                                    i--;
                                    countries.add(s);
                                }
                                i++;
                            }
                            ListView myListView = findViewById(R.id.myListView);
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, countries);
                            myListView.setAdapter(arrayAdapter);
                            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Toast.makeText(Search.this, countries.get(position), Toast.LENGTH_SHORT).show();
                                    Log.i("name: ",countries.get(position));
                                    Intent intent = new Intent(Search.this, Details.class);
                                    intent.putExtra("country", finalCountry);
                                    intent.putExtra("name", countries.get(position));
                                    startActivity(intent);
                                    finish();

                                }
                            });
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