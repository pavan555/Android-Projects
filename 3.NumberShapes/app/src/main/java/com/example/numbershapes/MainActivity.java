package com.example.numbershapes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void checkNumber(View view){
        EditText checkNumber = findViewById(R.id.enteredNumber);
        TextView resultTv = findViewById(R.id.resultText);
        String message;
        if(checkNumber.getText().toString().isEmpty()){
            message ="Please enter a Number";
        }else{
            Number number = new Number();
            number.setNumber(Integer.parseInt(checkNumber.getText().toString()));
            message = number.getNumber() + number.isSquare() + " and \n "+ number.isTriangular();
        }

        resultTv.setText(message);
        resultTv.setVisibility(View.VISIBLE);
    }
}

class Number{
    int number;
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public  String isTriangular(){
        int triangularNumber = 1;
        int x=1;

        while(number > triangularNumber){
            x++;
            triangularNumber +=x;
        }

        if(number == triangularNumber){
            return  " is triangular number";
        }
        return  " is not triangular number";
    }

    public String isSquare(){
        Double Squareroot = Math.sqrt(getNumber());
        if(Squareroot == Math.floor(Squareroot)){
            return " Square number";
        }
        return " not a square number";

    }



}
