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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView textViewName = (TextView) findViewById(R.id.name);
        TextView textViewCountry = (TextView) findViewById(R.id.country);
        TextView textViewDomain = (TextView) findViewById(R.id.domain);
        TextView textViewWebsite = (TextView) findViewById(R.id.website);
        TextView textViewCode = (TextView) findViewById(R.id.code);

        TextView textViewDetails = (TextView) findViewById(R.id.details);
        TextView textViewWifi = (TextView) findViewById(R.id.wifTtext);

        ImageView imageViewName = (ImageView) findViewById(R.id.imgName);
        ImageView imageViewCountry = (ImageView) findViewById(R.id.imgCountry);
        ImageView imageViewDomain = (ImageView) findViewById(R.id.imgDomain);
        ImageView imageViewWebpage = (ImageView) findViewById(R.id.imgWebpage);
        ImageView imageViewCode = (ImageView) findViewById(R.id.imgCode);

        ImageView imageViewWifi = (ImageView) findViewById(R.id.wifiImage);

        SQLiteDatabase myDB = this.openOrCreateDatabase("details", MODE_PRIVATE, null);

        Intent intent;
        intent = getIntent();
        String country = "";
        String name = "";
        country += intent.getStringExtra("country");
        name += intent.getStringExtra("name");
        String finalCountry = country;
        String finalName = name;

        OkHttpClient client = new OkHttpClient();
        String url = "http://universities.hipolabs.com/search?name=" + name + "&country=" + country;
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();

        String finalName1 = name;
        String finalCountry2 = country;
        String finalName2 = name;
        String finalName3 = name;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Details.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int i = 15;
                            String domains = "", alpha_two_code = "", web_pages="";
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
                            //show info
                            textViewName.setText(finalName1);
                            textViewCountry.setText(finalCountry2);
                            textViewDomain.setText(domains);
                            textViewWebsite.setText(web_pages);
                            textViewCode.setText(alpha_two_code);

                            textViewWifi.setVisibility(View.INVISIBLE);
                            imageViewWifi.setVisibility(View.INVISIBLE);

                            textViewName.setVisibility(View.VISIBLE);
                            textViewCountry.setVisibility(View.VISIBLE);
                            textViewDomain.setVisibility(View.VISIBLE);
                            textViewWebsite.setVisibility(View.VISIBLE);
                            textViewCode.setVisibility(View.VISIBLE);

                            textViewDetails.setVisibility(View.VISIBLE);

                            imageViewName.setVisibility(View.VISIBLE);
                            imageViewCountry.setVisibility(View.VISIBLE);
                            imageViewDomain.setVisibility(View.VISIBLE);
                            imageViewWebpage.setVisibility(View.VISIBLE);
                            imageViewCode.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Cursor resultSet = myDB.rawQuery("SELECT * FROM detail_universityy WHERE name=?", new String[]{finalName3});
                int y= resultSet.getCount();
                resultSet.moveToFirst();
                if (y > 0) {
                    //show data
                    while(!resultSet.isAfterLast()){
                        textViewName.setText(resultSet.getString(resultSet.getColumnIndex("name")));
                        textViewCountry.setText(resultSet.getString(resultSet.getColumnIndex("country")));
                        textViewDomain.setText(resultSet.getString( resultSet.getColumnIndex("domains")));
                        textViewWebsite.setText(resultSet.getString(resultSet.getColumnIndex("webpages")));
                        textViewCode.setText(resultSet.getString(resultSet.getColumnIndex("alpha_code")));

                        textViewWifi.setVisibility(View.INVISIBLE);
                        imageViewWifi.setVisibility(View.INVISIBLE);

                        textViewName.setVisibility(View.VISIBLE);
                        textViewCountry.setVisibility(View.VISIBLE);
                        textViewDomain.setVisibility(View.VISIBLE);
                        textViewWebsite.setVisibility(View.VISIBLE);
                        textViewCode.setVisibility(View.VISIBLE);

                        textViewDetails.setVisibility(View.VISIBLE);

                        imageViewName.setVisibility(View.VISIBLE);
                        imageViewCountry.setVisibility(View.VISIBLE);
                        imageViewDomain.setVisibility(View.VISIBLE);
                        imageViewWebpage.setVisibility(View.VISIBLE);
                        imageViewCode.setVisibility(View.VISIBLE);
                        resultSet.moveToNext();
                    }
                }
                resultSet.close();
            }

        });
    }
    }
}