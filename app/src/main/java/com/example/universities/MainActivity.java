package com.example.universities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length()>0 && !countries.contains(country)) {
                countries.add(country);
            }
        }

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.i("SELECTED TEXT WAS------->", arg0.getItemAtPosition(arg2).toString());
                Intent intent = new Intent(MainActivity.this, Search.class);
                intent.putExtra("country", arg0.getItemAtPosition(arg2).toString());
                startActivity(intent);
                finish();

            }
        });
    }
}