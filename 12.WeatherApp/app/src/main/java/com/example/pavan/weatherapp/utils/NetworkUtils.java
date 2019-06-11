package com.example.pavan.weatherapp.utils;

import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;


public class NetworkUtils {

    public static final String WEATHER_API_URL = "http://api.openweathermap.org/data/2.5/weather";
    public static final String APPID ="b97f3dfd4f01d1ae78cac9fedece8ea0";
    public static final String QUERY_API_KEY = "APPID";
    public static final String QUERY_CITY = "q";



    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;



    public static URL buildURL(String string){
        Uri uri = Uri.parse(WEATHER_API_URL).buildUpon()
                .appendQueryParameter(QUERY_CITY,string)
                .appendQueryParameter(QUERY_API_KEY,APPID).build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static Spanned JsonBuilder(JSONObject jsonObject) throws JSONException {
        String country = jsonObject.getJSONObject("sys").getString("country");
        String description = jsonObject.getJSONArray("weather")
                .getJSONObject(0).getString("description");
        Double temp = (jsonObject.getJSONObject("main").getDouble("temp")-273.15);
        Double lowTemp =jsonObject.getJSONObject("main").getDouble("temp_min")-273.15;
        Double highTemp =jsonObject.getJSONObject("main").getDouble("temp_max")-273.15;
        Double windspeed = jsonObject.getJSONObject("wind").getDouble("speed");
        Double Pressure = jsonObject.getJSONObject("main").getDouble("pressure");
        Double humidity = jsonObject.getJSONObject("main").getDouble("humidity");
        Double cloudy = jsonObject.getJSONObject("clouds").getDouble("all");
        Double lat = jsonObject.getJSONObject("coord").getDouble("lat");
        Double lon = jsonObject.getJSONObject("coord").getDouble("lon");
        String city = jsonObject.getString("name");

        String html = String.format(Locale.getDefault(),"<font color=\"#DA4213\"> %s %s </font><i>%s</i><br><font color=\"#ffc107\"><b> %.2f&#176;C</b></font> <font color=\"#28a745\" >" +
                        "temperature from %.2f to %.2f \u00B0C, Wind %.2f m/s. clouds %.2f%%, humidity %.2f %% , %.2f hpa</font><br>" +
                        "Geo Coords  <font color=\"red\">"+"[%.2f , %.2f]</font>",
                city,country,description,temp,lowTemp,highTemp,windspeed,cloudy,humidity,Pressure,lat,lon);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
            // we are using this flag to give a consistent behaviour
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }



    public static JSONObject getResponseFromHttpConnection(URL url) throws IOException {
        String result = null;
        String data;
        JSONObject jsonObject = new JSONObject();


        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.connect();

        httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
        httpURLConnection.setRequestMethod(REQUEST_METHOD);
        httpURLConnection.setReadTimeout(READ_TIMEOUT);


        InputStream inputStream = httpURLConnection.getInputStream();
        InputStreamReader scanner = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(scanner);
        StringBuilder stringBuilder = new StringBuilder();

        while((data = reader.readLine()) != null){
            stringBuilder.append(data);
        }

        reader.close();
        scanner.close();

        result = stringBuilder.toString();

        try {
            jsonObject = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }



}
