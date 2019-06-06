package com.example.guessme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    final int randomNumber = getRandomNumber(100);

    int noOfGuesses = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void isGuessCorrect(View view){

        Log.i("Random Number ", String.valueOf(randomNumber));
        EditText guessTv = findViewById(R.id.guessNumber);
        TextView countTv = findViewById(R.id.guessCount);
        int guessNumber = Integer.parseInt(guessTv.getText().toString());

        if (guessNumber == randomNumber) {
            Toast.makeText(this, "you Guessed it right", Toast.LENGTH_SHORT).show();
            countTv.setText(String.valueOf(noOfGuesses+1));
            noOfGuesses=0;
        } else {
            if (guessNumber > randomNumber) {
                Toast.makeText(this, "Lower!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Higher!!", Toast.LENGTH_SHORT).show();
            }
            noOfGuesses+=1;
            countTv.setText(String.valueOf(noOfGuesses));
        }




    }

    public int getRandomNumber(int max){
        Random r = new Random();
        return r.nextInt(max)+1;
    }
}
