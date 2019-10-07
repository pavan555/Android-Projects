package com.example.pavan.news.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class NetworkUtils {
    private final static String BASE_URL="https://newsapi.org/v2/top-headlines";
    //private final static String SOURCES_QUERY="sources"; //optional parameter
    private final static String API_KEY_QUERY="apiKey";//X-Api-Key if used in HttpHeaders
    private final static String API_KEY="b1cb530f17e142d7b1d4d51a667f76c3";




    public static URL buildURL(){
        Uri uri=Uri.parse(BASE_URL).buildUpon()
                //.appendQueryParameter(SOURCES_QUERY,"google-news")
                .appendQueryParameter("country","in")
                .appendQueryParameter(API_KEY_QUERY,API_KEY)
                .build();
        URL url=null;
        try {
            url=new URL(uri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static JSONObject getResponseFromHttp(URL url) throws IOException {


        HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(15000);
        urlConnection.setConnectTimeout(15000);
        urlConnection.connect();

        InputStream inputStream=urlConnection.getInputStream();
        InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
        BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
        StringBuilder string = new StringBuilder();


        String data;
        while((data=bufferedReader.readLine())!=null){
            string.append(data);
        }

        bufferedReader.close();
        inputStreamReader.close();
        JSONObject jsonObject=null;
        try {
             jsonObject=new JSONObject(string.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static Bitmap getImageFromHttpConnection(String string) {
        Bitmap bitmap;
        try {
            URL url = new URL(string);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);

            return bitmap;
        }catch (Exception e){
            return null;
        }

    }

}
