package com.example.pavan.braintrainer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button goButton,option0,option1,option2,option3,playAgain;
    List<Integer> options = new ArrayList<Integer>();
    TextView resultTv,scoreTv,question,counterTv;
    ConstraintLayout constraintLayout;
    int locationOfAnswer;
    int scores;
    int numberOfQuestions = 0;
    boolean active = false;


    public  void isItCorrect(View view){
        Button option = (Button) view;
        if(Integer.parseInt(option.getTag().toString()) == locationOfAnswer){
            resultTv.setText(getResources().getString(R.string.result));
            scores++;
        }else{
            resultTv.setText("Wrong  :(");
        }
        numberOfQuestions++;

        scoreTv.setText(String.format("  %s/%s  ",scores,numberOfQuestions));
        nextQuestion();

    }

    public void startGame(View view){
        scores =0;
        numberOfQuestions =0;

        playAgain.setVisibility(View.INVISIBLE);
        counterTv.setText("30s");
        scoreTv.setText(String.format("  %s/%s  ",scores,numberOfQuestions));
        resultTv.setText("");
        option0.setEnabled(true);
        option1.setEnabled(true);
        option2.setEnabled(true);
        option3.setEnabled(true);

        nextQuestion();
        new CountDownTimer(30100,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                if(millisUntilFinished/1000 < 10){
                    counterTv.setText(String.format("0%ss",millisUntilFinished/1000));
                }else{
                    counterTv.setText(String.format("%ss",millisUntilFinished/1000));
                }

            }

            @Override
            public void onFinish() {

                counterTv.setText("00s");
                option0.setEnabled(false);
                option1.setEnabled(false);
                option2.setEnabled(false);
                option3.setEnabled(false);
                resultTv.setText("Done !!");
                playAgain.setVisibility(View.VISIBLE);

                Log.i("finish","Finished");

            }
        }.start();

    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //finding ids
        goButton = findViewById(R.id.startButton);
        question = findViewById(R.id.questionTv);
        option0 = findViewById(R.id.option0);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        resultTv = findViewById(R.id.resultTv);
        scoreTv = findViewById(R.id.answersCount);
        counterTv= findViewById(R.id.countdownTimer);
        playAgain = findViewById(R.id.play);
        constraintLayout = findViewById(R.id.game);

        goButton.setVisibility(View.VISIBLE);
        constraintLayout.setVisibility(View.INVISIBLE);

    }

    private void nextQuestion() {
        options.clear();
        //generating random numbers for question
        Random random = new Random();
        int a = random.nextInt(22);
        int b = random.nextInt(21);

        question.setText(String.format("%s + %s  = ?",a,b));
        locationOfAnswer = random.nextInt(4);
        int answer = a+b;

        for(int i=0;i<4;i++){
            if(i == locationOfAnswer){
                options.add(answer);
            }else{
                int wrongAnswer = random.nextInt(40)+1;

                while(wrongAnswer == answer || options.contains(wrongAnswer)) {
                    wrongAnswer = random.nextInt(41);
                }
                options.add(wrongAnswer);
            }
        }

        option0.setText(String.format("%s",options.get(0)));
        option1.setText(String.format("%s",options.get(1)));
        option2.setText(String.format("%s",options.get(2)));
        option3.setText(String.format("%s",options.get(3)));
    }

    public void start(View view) {
        goButton.setVisibility(View.INVISIBLE);
        constraintLayout.setVisibility(View.VISIBLE);
        startGame(goButton);
    }


}



