package com.example.universities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
    ArrayList<String> countries = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        SQLiteDatabase myDB = this.openOrCreateDatabase("details", MODE_PRIVATE, null);

        Intent intent;
        intent = getIntent();
        String country="";
        country+= intent.getStringExtra("country");

        String finalCountry = country;

        OkHttpClient client = new OkHttpClient();
        String url = "http://universities.hipolabs.com/search?country="+country;
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();

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
                            for(int j=0;j<20;j++){
                                okhttp3.Request request1 = new okhttp3.Request.Builder().url("http://universities.hipolabs.com/search?name="+countries.get(j)+"&country="+ finalCountry).build();
                                int finalJ = j;
                                client.newCall(request1).enqueue(new Callback() {
                                    @Override
                                    public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                                        if (response.isSuccessful()) {
                                            final String myResponse = response.body().string();
                                            Search.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
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
                                                    String s = countries.get(finalJ);
                                                    Log.i("s", s);
                                                    String v= finalCountry;

                                                    myDB.execSQL("CREATE TABLE IF NOT EXISTS detail_universityy (name VARCHAR PRIMARY KEY, country VARCHAR, domains VARCHAR, webpages VARCHAR, alpha_code VARCHAR)");
                                                    try{
                                                        myDB.execSQL("INSERT INTO detail_universityy  VALUES ( '"+s+"' , '"+v+"' ,'"+domains+"', '"+web_pages+"', '"+alpha_two_code+"')");
                                                    }
                                                    catch(Exception e){

                                                    }
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
                //no data available
                Toast.makeText(Search.this, "No data available!", Toast.LENGTH_SHORT).show();
            }

        });

    }
    public void countryAgain(View view){
        //go back to search country
        Intent intent = new Intent(Search.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}