package edu.umich.akupatel.myapplication;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    Button getid, getbyid, getbyname;
    EditText etxt;
    static ListView listView;
    WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getid =  findViewById(R.id.btn_getCityID);
        getbyid = findViewById(R.id.btn_getByCityID);
        getbyname = findViewById(R.id.btn_getByName);
        etxt = findViewById(R.id.etxt);
        listView = findViewById(R.id.mylist);

        ArrayList arr = new ArrayList<String>();



        getbyid.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               weatherDataService.getCityForecastByID(etxt.getText().toString());

           }
        });

        getid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = etxt.getText().toString();
                weatherDataService.getCityID(cityName);
            }
        });
        getbyname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,""+etxt.getText(),Toast.LENGTH_SHORT).show();

            }
        });
        }}
