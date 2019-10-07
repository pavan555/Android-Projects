package com.example.pavan.news;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pavan.news.utilities.DatabaseUtils;
import com.example.pavan.news.utilities.NetworkUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //private final static String DSN="https://240a32170b7848e9a0048647fe5b7c1@sentry.io/1551212";
    public static SQLiteDatabase sqLiteDatabase;
    private static ImageView imageView;
    private static TextView title,desc,source;
    public static Resources resources;
    public static int first,last,id;
    public static CountDownTimer countDownTimer;
    private static Random random=new Random();
    private static Cursor mainCursor;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sentry.init(DSN, new AndroidSentryClientFactory(this));
        imageView= findViewById(R.id.imageView);
        title = findViewById(R.id.title);
        desc=findViewById(R.id.desc);
        source=findViewById(R.id.source);
        resources=getResources();


        sqLiteDatabase=this.openOrCreateDatabase("News",MODE_PRIVATE,null);
        //sqLiteDatabase.execSQL("DROP TABLE articles");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS articles(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,source VARCHAR,title VARCHAR,description VARCHAR,url VARCHAR,urlToImage VARCHAR,publishedTime DATETIME,image BLOB DEFAULT NULL)");
        Cursor c = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM articles", null);
        c.moveToFirst();
        first=5;
        last=10;

        countDownTimer = new CountDownTimer(3000000,10000){

            @Override
            public void onTick(long millisUntilFinished) {

                try {
                    setActivity(23);

                } catch (Exception e) {
                    countDownTimer.onFinish();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFinish() {
                setActivity(-1);
                Log.i("finished","finished");


            }
        };

        if(c.getInt(0)<1){
            new network().execute(NetworkUtils.buildURL());
        }else {
            Log.i("TOTAL COUNT",Integer.toString(c.getInt(0)));
            mainCursor = DatabaseUtils.select(first,last);
            Log.i("COUNT CURSOR",mainCursor.getColumnCount()+" "+mainCursor.getCount());


            Log.i("MAIN CURSOR ID", String.valueOf(mainCursor.getInt(0)));
            countDownTimer.start();
        }
        c.close();


    }

    public static void setActivity(int next) {

        if(next >1){
            mainCursor.moveToNext();
        }else if (next==0){
            mainCursor.moveToPrevious();
        }else if(next==1){
            mainCursor.moveToPosition(id);
        }else if(next==-1){
            mainCursor=DatabaseUtils.select(0,0);
            countDownTimer.start();
        }


        //for(;(!mainCursor.equals(null) || mainCursor.getColumnCount()!=8) && mainCursor.isLast();mainCursor.moveToNext());
//        if(mainCursor.getPosition()==last-1){
//            mainCursor=DatabaseUtils.select(first,last);
//        }




        if(!mainCursor.isLast()) {
            if(mainCursor.getBlob(7)==null){
                imageView.setImageDrawable(resources.getDrawable(R.drawable.noimage));
            }else {
                //Log.i("BITMAP", String.valueOf(BitmapFactory.decodeByteArray(mainCursor.getBlob(7),0,mainCursor.getBlob(7).length)));
                //decoding is done here
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(mainCursor.getBlob(7),0,mainCursor.getBlob(7).length));
            }
            desc.setText(mainCursor.getString(3));
            desc.append(resources.getString(R.string.read_more));
            title.setText(mainCursor.getString(2));
            source.setText(R.string.author);
            source.append(mainCursor.getString(1));
            Log.i("MAIN CURSOR ID", String.valueOf(mainCursor.getInt(0)));

        }else{
            mainCursor.moveToPosition(random.nextInt(mainCursor.getCount())+1);
        }
    }


    public static class network extends AsyncTask<URL,Void, JSONObject>{


        @Override
        protected JSONObject doInBackground(URL... urls) {
            JSONObject jsonObject=null;

            try {
                jsonObject=NetworkUtils.getResponseFromHttp(urls[0]);
            } catch (IOException e) {
                //Sentry.capture(e);
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Log.i("JSONOBJECT", String.valueOf(jsonObject));
            DatabaseUtils.checkorInsertData(jsonObject);
        }
    }

    public static class imageDownloader extends AsyncTask<String,Void, Bitmap>{


        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap=null;
            if(!strings[0].equals(null)){
                try {
                    bitmap = NetworkUtils.getImageFromHttpConnection(strings[0]);
                } catch (Exception e) {
                    //Sentry.capture(e);
                    e.printStackTrace();
                }
            }
            return bitmap;
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.refresh:
                new network().execute(NetworkUtils.buildURL());
                return true;
             default:
                 return false;
            
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==101){
            if(resultCode==Activity.RESULT_OK){
                Log.i("RESULT", String.valueOf(mainCursor.getInt(0)));

            }
            else{
                Log.i("UNSUCCESSFUL","unsuccessful");
                try {
                    setActivity(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void getWebView(View view){

            Intent intent = new Intent(MainActivity.this, WebActivity.class);
            try {
                id=mainCursor.getInt(0);
                intent.putExtra("id", id);
                intent.putExtra("url", mainCursor.getString(4));
                startActivityForResult(intent, 101);
            }catch (Exception e){
                Log.i("News: ","NO DATA");
                mainCursor.moveToFirst();
            }

    }
}
