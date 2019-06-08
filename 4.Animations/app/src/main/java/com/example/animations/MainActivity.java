package com.example.animations;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    boolean ashVisible = true;

    public void fade(View view){

        ImageView ash = findViewById(R.id.ashImageView);
        ImageView pikachu = findViewById(R.id.pikachuImageView);


        if(ashVisible){
            ashVisible=false;
            ash.animate().scaleX(1).scaleY(1).alpha(0).setDuration(2000);
            pikachu.animate().alpha(1).scaleX(0.5f).scaleY(0.5f).setDuration(2000);
        }else{
            ashVisible=true;
            pikachu.animate().scaleX(1).scaleY(1).alpha(0).setDuration(2000);
            ash.animate().alpha(1).scaleX(0.5f).scaleY(0.5f).setDuration(2000);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView ash = findViewById(R.id.ashImageView);
        ash.setX(-1000);
        ash.animate().translationXBy(1000).rotation(3600).setDuration(2500);

    }
}
