package com.example.pavan.snapchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class DisplaySnapActivity extends AppCompatActivity {


    ImageView displaySnapImage;
    TextView displayMessage;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_snap);

        displayMessage=findViewById(R.id.displayMessageText);
        displaySnapImage=findViewById(R.id.displaySnapImage);
        progressBar=findViewById(R.id.progressBar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            progressBar.setTooltipText("Downloading Image");
        }


        firebaseAuth=FirebaseAuth.getInstance();
        displayMessage.setText(getIntent().getStringExtra("message"));


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        new ImageDownloader().execute(getIntent().getStringExtra("imageURL"));
                    }
                catch (Exception e) {
                    e.printStackTrace();
                }
                }
            });



    }



    public class ImageDownloader extends AsyncTask<String ,Void ,Bitmap>{

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {

            try {
                URL url=new URL(strings[0]);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setReadTimeout(15000);
                Bitmap bitmap;

                httpURLConnection.connect();
                InputStream inputStream=httpURLConnection.getInputStream();
                bitmap= BitmapFactory.decodeStream(inputStream);

                return bitmap;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            progressBar.setVisibility(View.INVISIBLE);
            displaySnapImage.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onBackPressed() {
        FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.getUid()).child("snaps").child(getIntent().getStringExtra("snapKey")).removeValue();
        FirebaseStorage.getInstance().getReference().child("Images").child(getIntent().getStringExtra("imageName")).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
            }
        });
    }
}
