package com.example.pavan.guessthecelebrity;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pavan.guessthecelebrity.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    final String CELEB_SEARCH_URL = "http://www.posh24.se/kandisar";
    String htmlPage = "";
    Random random = new Random();
    int choosenCelebLocation,choosenCeleb;

    List<String> celebImageUrls = new ArrayList<String>();
    List<String> celebNames = new ArrayList<String>();
    List<String> options = new ArrayList<String>();


    Pattern Imagepattern = Pattern.compile("<img src=\"(.*?)\"");
    Pattern namePattern = Pattern.compile(" alt=\"(.*?)\"");

    ImageView imageView;
    Button option0,option1,option2,option3,tryAgain;
    TextView textView;
    ConstraintLayout constraintLayout;




    public void guessCeleb(View view){

        Button button = (Button) view;
        if(String.valueOf(choosenCelebLocation).equals(button.getTag().toString())){
            Toast.makeText(getApplicationContext(),"Correct :)",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"Wrong (: It was "+celebNames.get(choosenCeleb),Toast.LENGTH_SHORT).show();
        }

        generateCeleb();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        option0 = findViewById(R.id.option0);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        textView = findViewById(R.id.textView);
        constraintLayout = findViewById(R.id.content);
        tryAgain = findViewById(R.id.tryAgain);

        HtmlPage s = new HtmlPage();


        try {
             htmlPage = s.execute(CELEB_SEARCH_URL).get();
             htmlPage = htmlPage.split("<div class=\"listedArticles\">")[0];
             if(htmlPage != null || !htmlPage.equals("")){
                Matcher m = Imagepattern.matcher(htmlPage);
                while (m.find()){
                    celebImageUrls.add(m.group(1));
                }
                m = namePattern.matcher(htmlPage);
                while (m.find()){
                    celebNames.add(m.group(1));
                }
                if(celebNames.size() != 0 && celebImageUrls.size()!=0) {
                    generateCeleb();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void showData(){
        textView.setVisibility(View.INVISIBLE);
        tryAgain.setVisibility(View.INVISIBLE);
        constraintLayout.setVisibility(View.VISIBLE);

    }

    public void showError(){
        textView.setVisibility(View.VISIBLE);
        tryAgain.setVisibility(View.VISIBLE);
        constraintLayout.setVisibility(View.INVISIBLE);

    }

    public void TryAgain(View view){
        if(htmlPage == null || htmlPage.equals("")){
            HtmlPage s = new HtmlPage();
            try {
                htmlPage = s.execute(CELEB_SEARCH_URL).get();
                htmlPage = htmlPage.split("<div class=\"listedArticles\">")[0];
                Matcher m = Imagepattern.matcher(htmlPage);
                while (m.find()){
                    celebImageUrls.add(m.group(1));
                }
                m = namePattern.matcher(htmlPage);
                while (m.find()){
                    celebNames.add(m.group(1));
                }
                if(celebNames.size() != 0 && celebImageUrls.size()!=0) {
                    generateCeleb();
                    }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }else{
            generateCeleb();
        }
    }

    public void generateCeleb(){

        choosenCeleb = random.nextInt(celebNames.size());
        choosenCelebLocation =random.nextInt(4);
        options.clear();
        int randomGuess;

        for(int i=0;i<4;i++){
            if(i == choosenCelebLocation){
                options.add(celebNames.get(choosenCeleb));
                try {
                    ImageDownloader imageDownloader =new ImageDownloader();
                    Bitmap bitmap =imageDownloader.execute(celebImageUrls.get(choosenCeleb)).get();
                    imageView.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                randomGuess = random.nextInt(celebNames.size());
                while(randomGuess == choosenCeleb || options.contains(celebNames.get(randomGuess))){
                    randomGuess = random.nextInt(celebNames.size());
                }
                options.add(celebNames.get(randomGuess));
            }
        }

        option0.setText(options.get(0));
        option1.setText(options.get(1));
        option2.setText(options.get(2));
        option3.setText(options.get(3));

    }



    public  class ImageDownloader extends AsyncTask<String,Void,Bitmap> {


        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            try {
                bitmap = NetworkUtils.getImageFromHttpConnection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if(bitmap != null){
                showData();
            }else{
                showError();
            }
            super.onPostExecute(bitmap);
        }
    }


    public class HtmlPage extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String s;
            try {
                s= NetworkUtils.getResponseFromHttpConnection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            return s;
        }

        @Override
        protected void onPostExecute(String s) {

            if(s !=null ){
                showData();
            }else{
                showError();
            }
            super.onPostExecute(s);
        }
    }


}
