package com.example.pavan.webcontentsdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.pavan.webcontentsdemo.utilities.NetworkUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {


    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);

    }

    public void downloadImage(View view){
        new DownloadContent().execute("https://vignette.wikia.nocookie.net/p__/images/7/79/Ben_10_OS.png/revision/latest?cb=20180830092243&path-prefix=protagonist");

    }


    public class DownloadContent extends AsyncTask<String,Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {
            URL url = null;
            Bitmap bitmap =null;

            try {
                url = new URL(urls[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap!=null){
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}

