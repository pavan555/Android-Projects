package com.example.pavan.guessthecelebrity.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class NetworkUtils {


    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;




    public static String getResponseFromHttpConnection(String string) throws IOException {
        URL url = null;
        String result = null;
        String data;
        try {
            url = new URL(string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

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

        return result;
    }


    public static Bitmap getImageFromHttpConnection(String string) throws IOException {
        URL url = null;
        try {
            url = new URL(string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.connect();

        httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
        httpURLConnection.setRequestMethod(REQUEST_METHOD);
        httpURLConnection.setReadTimeout(READ_TIMEOUT);

        InputStream inputStream = httpURLConnection.getInputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        return bitmap;

    }



}
