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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent;
        intent = getIntent();
        String country="";
        country+= intent.getStringExtra("country");

        ArrayList<String> myShows = new ArrayList<String>();
        myShows.add("Greys Anatomy");

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://universities.hipolabs.com/search?country=" + country;
        Log.i("RRRRR", url);
        Log.i("hhhh", "jjjj");

//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
////                        JSONArray jArray = response.toJSONArray(new JSONArray);
////                        if (jArray != null) {
////                            for (int i=0;i<jArray.length();i++){
////                                try {
////                                    myShows.add(jArray.get(i).toString());
////                                } catch (JSONException e) {
////                                    e.printStackTrace();
////                                }
////                            }
////                        }
//                        Log.i("JJJJJJJJJJJJ", response.toString());
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.i("ERRRRRRRRRRRR", error.toString());
//
//                    }
//                });
//        ListView myListView = findViewById(R.id.myListView);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myShows);
//        myListView.setAdapter(arrayAdapter);
//        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, myShows.get(position), Toast.LENGTH_SHORT).show();
//                Log.i("show name",myShows.get(position));
//            }
//        });
//    }
    }
}