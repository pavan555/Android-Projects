package com.example.pavan.eggtimer;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView countDown;
    MediaPlayer mediaPlayer;
    SeekBar timeSeekBar;
    Button alarmButton;
    boolean alarmActive = false;
    CountDownTimer count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         timeSeekBar = findViewById(R.id.countDownSeekBar);//max 10mins
         countDown= findViewById(R.id.countDownTimer);
         mediaPlayer = MediaPlayer.create(this,R.raw.buzz);
        alarmButton = findViewById(R.id.timeButton);

        timeSeekBar.setProgress(300);
        updateTimer(300);


        timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTimer(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




    }


    private void updateTimer(int progress) {

        int minutes = progress/60;
        int seconds = progress - (minutes*60);
        timeSeekBar.setProgress(progress);
        countDown.setText(String.format("%s:%s",addZeros(minutes),addZeros(seconds)));

    }
    public String addZeros(int number){

        String num = Integer.toString(number);
        if(num.length() == 1){
            return "0"+num;
        }
        return num;
    }

    public void alaramStart(View view){


        if(!alarmActive && timeSeekBar.getProgress() != 0) {
            alarmActive = true;
            timeSeekBar.setEnabled(false);
            alarmButton.setText("STOP!!");

            count = new CountDownTimer(timeSeekBar.getProgress() * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    updateTimer((int) (millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    //Log.i("FInish", "finished");
                    mediaPlayer.start();//playing audio
                    timeSeekBar.setProgress(0); //setting progess as finished
                }
            };
            count.start();


        }else{
            mediaPlayer.stop();
            alarmActive= false;
            timeSeekBar.setEnabled(true);
            alarmButton.setText(getResources().getString(R.string.buttonText));
            if (timeSeekBar.getProgress() != 0) {
                count.cancel();
            }

        }
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
