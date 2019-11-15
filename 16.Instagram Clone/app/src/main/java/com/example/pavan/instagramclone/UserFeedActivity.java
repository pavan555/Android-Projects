package com.example.pavan.instagramclone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pavan.instagramclone.Utils.ImageAdapter;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ProgressCallback;

import java.util.ArrayList;
import java.util.List;

public class UserFeedActivity extends AppCompatActivity {

    private static int NUM_LIST_ITEMS;
    private  ImageAdapter imageAdapter;
    private RecyclerView recyclerView;

    public static List<Bitmap> BytesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        BytesList=new ArrayList<>();
        String user=getIntent().getStringExtra("username");
        setTitle(user);

        final ConstraintLayout constraintLayout=findViewById(R.id.contraintLayout);

        recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setAdapter(imageAdapter);



        ParseQuery<ParseObject> query=ParseQuery.getQuery("Images");

        query.whereEqualTo("username",user);
        query.addDescendingOrder("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null) {
                    if (objects.size() >0 ) {
                        MainActivity.showToast(String.valueOf(objects.size()), UserFeedActivity.this);

                        Log.i("Objects are: ",String.format("%s",objects));
                        for (ParseObject object : objects) {
                            ParseFile file = object.getParseFile("image");
                            // ParseFile file = new ParseFile(("parse"+String.valueOf(object.getParseFile("image")).substring(8)).getBytes());

                            Log.i("OBJECT URL", file.getUrl());

                            //error here file url is wrong

                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null) {
                                        if (data.length > 0) {
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                            BytesList.add(bitmap);
                                        }
                                    } else {
                                        MainActivity.showToast(e.getMessage(), UserFeedActivity.this);
                                    }
                                    //TODO(1) Solve the error here when you visit nextTime


                                }
                            }, new ProgressCallback() {
                                @Override
                                public void done(Integer percentDone) {
                                      MainActivity.showToast(percentDone.toString(),UserFeedActivity.this);
                                }
                            });



                        }



                    }else {
                        constraintLayout.setVisibility(View.VISIBLE);
                        //recyclerView.setVisibility(View.GONE);
                        TextView textView=new TextView(getApplicationContext());
                        textView.setLayoutParams(new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        ));
                        textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                        textView.setText(getResources().getString(R.string.no_feed));
                        textView.setLineSpacing(5,5);
                        textView.setTextSize(26);
                        textView.setTypeface(Typeface.SANS_SERIF,Typeface.ITALIC);
                        constraintLayout.addView(textView);
                    }
                }else{
                        e.printStackTrace();
                    }
                }
        });



    }
}
