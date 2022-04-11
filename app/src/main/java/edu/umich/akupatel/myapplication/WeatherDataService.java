package edu.umich.akupatel.myapplication;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {
    public static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERY_FOR_CITY_WEATHER_BY_ID = "https://www.metaweather.com/api/location/";
    public static final String API_KEY = ",us&appid=aa4d445547a1bb0d569f46d3a4470ab5"; //Open Weather API Key
    public static final String OPEN_WEATHER_MAP_URL = "https://api.openweathermap.org/data/2.5/weather?zip=";


    ArrayList arr = new ArrayList();
    String cityID, title = "";
    Context context;

    public WeatherDataService(Context context) {
        this.context = context;
    }



    public void getCityID(String zipcode){
        ArrayList listarr = new ArrayList();
       // String url = QUERY_FOR_CITY_ID + cityName;
        String openurl = OPEN_WEATHER_MAP_URL + zipcode + API_KEY;


        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, openurl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String city = response.getString("name"); //Get City name from Zip Code
                            JSONArray weatherdata = response.getJSONArray("weather");
                            JSONObject weather_info = weatherdata.getJSONObject(0);
                            String weather_description = weather_info.getString("description");

                            JSONObject main_info = response.getJSONObject("main");
                            String temp = main_info.getString("temp");

                            String for_arrlist = "City: "+city+
                                    "\n"+"Weather State: "+weather_description+"\n"+
                                    "Temperature: "+temp;
                            listarr.add(for_arrlist);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,listarr);
                        MainActivity.listView.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Cannot get it", Toast.LENGTH_SHORT).show();
                        // TODO: Handle error

                    }
                });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void getCityForecastByID(String cityID) {
        ArrayList listarr = new ArrayList();
        //List<WeatherReportModel> report = new ArrayList<WeatherReportModel>();
        String url = QUERY_FOR_CITY_WEATHER_BY_ID + cityID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                String arrlist_string = "";
                try {
                    JSONArray weather_data = response.getJSONArray("consolidated_weather");
                    String title = response.getString("title");
                    //Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
                    //WeatherReportModel weatherReportModel = new WeatherReportModel();

                    for (int i = 0; i < response.length() - 1; i++) {
                        JSONObject weatherFromAPI = (JSONObject) weather_data.getJSONObject(i);
//                      weatherReportModel.setId(weatherFromAPI.getInt("id"));
//                      weatherReportModel.setApplicable_date(weatherFromAPI.getString("applicable_date"));
//                      weatherReportModel.setWeather_state_name(weatherFromAPI.getString("weather_state_name"));
//                      weatherReportModel.setThe_temp(weatherFromAPI.getLong("the_temp"));

                        String date = weatherFromAPI.getString("applicable_date");
                        String weather_state_name = weatherFromAPI.getString("weather_state_name");
                        long the_temp = weatherFromAPI.getLong("the_temp");
                        int humidity = weatherFromAPI.getInt("humidity");

                        arrlist_string = "Date: " + date + "\n" +
                                "Weather State: " + weather_state_name + "\n" +
                                "Temperature: " + the_temp + "\n" +
                                "Humidity: " + humidity + "\n" +
                                "City Name: " + title;

                        listarr.add(arrlist_string);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayAdapter adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listarr);
                MainActivity.listView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void getWeatherByName(String cityName){

        String url = QUERY_FOR_CITY_ID + cityName;

        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject cityInfo;
                            for (int i = 0; i < response.length(); i++) {
                                cityInfo = response.getJSONObject(i);
                                cityID = cityInfo.getString("woeid");
                                getCityForecastByID(cityID);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Cannot get it", Toast.LENGTH_SHORT).show();
                        // TODO: Handle error

                    }
                });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}
