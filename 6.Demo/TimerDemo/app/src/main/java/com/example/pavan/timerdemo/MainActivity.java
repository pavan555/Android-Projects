package com.example.pavan.timerdemo;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Timers in Android we can do this in 2 types
        //One Handler and Runnable

//
//        final Handler handler = new Handler();
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                Log.i("Count Secs:","Second passed");
//                handler.postDelayed(this,1000);
//
//            }
//        };
//        handler.post(runnable);

        //Second one CountDownTimer and implementing the methods of CountDownTimer(Tick,Finish)

        //implemented for 10secs called when a sec passed
        new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i("CountDown:",String.valueOf(millisUntilFinished/1000));

            }

            @Override
            public void onFinish() {
                Log.i("Finish","finished");

            }
        }.start();

    }
}
