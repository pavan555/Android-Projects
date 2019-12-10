package com.example.pavan.news.utilities;


import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.pavan.news.MainActivity;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DatabaseUtils {

    public static void checkorInsertData(JSONObject jsonObject) {

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");//2019-10-02T15:40:00+00:00
        input.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {

            if (jsonObject.getString("status").equals("ok")) {
                //change iterator value if there are more than 20 results are coming
                String sqlStatement="INSERT INTO articles(source,title,description,url,urlToImage,publishedTime,image) VALUES(?,?,?,?,?,?,?)";
                SQLiteStatement sqLiteStatement=MainActivity.sqLiteDatabase.compileStatement(sqlStatement);
                for (int i = 0; i < 20; i++) {
                    JSONObject articles = jsonObject.getJSONArray("articles").getJSONObject(i);
                    Date date = input.parse(articles.getString("publishedAt"));

                    int count = MainActivity.sqLiteDatabase.rawQuery("SELECT * from articles where url LIKE \"" + articles.getString("url") + "\"", null).getCount();

                    if (count < 1) {

                        //source
                        String source=(articles.getString("author").equals("null")?articles.getJSONObject("source").getString("name"):articles.getString("author")).replaceAll("\"", "\"\"");
                        //title
                        String title=articles.getString("title").replaceAll("\"", "\"\"");
                        String desc="No Description :(";

                        //description
                        if(!articles.getString("description").equals("null")){
                            desc=articles.getString("description").replaceAll("\"", "\"\"");
                            if(desc.length()<200 && (!articles.getString("content").equals("null"))){
                                desc=desc+"\n\t"+articles.getString("content").substring(0,articles.getString("content").length()-13).replaceAll("\"", "\"\"");
                            }
                        }else if(!articles.getString("content").equals("null")){
                            desc=articles.getString("content").substring(0,articles.getString("content").length()-13).replaceAll("\"", "\"\"");
                        }


                        Log.d("SQL","INSERTING DATA "+articles.getString("urlToImage") +" "+articles.getString("url") );
                        sqLiteStatement.bindString(1,source);
                        sqLiteStatement.bindString(2,title);
                        sqLiteStatement.bindString(3,desc);
                        sqLiteStatement.bindString(4,articles.getString("url"));
                        sqLiteStatement.bindString(5,articles.getString("urlToImage"));
                        sqLiteStatement.bindString(6,output.format(date));
                        if(Encoder(new MainActivity.imageDownloader().execute(articles.getString("urlToImage")).get())!=null)
                            sqLiteStatement.bindBlob(7, Encoder(new MainActivity.imageDownloader().execute(articles.getString("urlToImage")).get()));

                        sqLiteStatement.execute();

                    }

                }
                MainActivity.setActivity(-1);
            }
        } catch (Exception e) {
            //Sentry.capture(e);
            e.printStackTrace();

        }
    }

    /*Encoding image*/
    private static byte[] Encoder(Bitmap bitmap) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            if(bitmap!=null){
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                //System.out.println(Arrays.toString(outputStream.toByteArray()));
                return outputStream.toByteArray();
            }
            return null;
    }

    /*
    DECODING IMAGE
    */
    public static Bitmap Decoder(String string){
        byte[] decoded = string.getBytes();
        //Log.i("IMAGE ARAAY",Arrays.toString(decoded));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        return BitmapFactory.decodeByteArray(decoded, 0, decoded.length,options);
    }

    /* SELECTION WILL BE DONE HERE */
    public static Cursor select(int first,int last){
        Cursor c=MainActivity.sqLiteDatabase.rawQuery("select * from articles order by publishedTime desc",null);
        c.moveToFirst();
        MainActivity.first=last+1;
        MainActivity.last=last+6;
        return c;
    }



}