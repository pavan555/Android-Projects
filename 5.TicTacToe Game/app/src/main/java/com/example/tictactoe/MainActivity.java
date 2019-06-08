package com.example.tictactoe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    //o for yellow 1 for red -1 for empty

    int activePlayer = 0;

    List<Integer> playingPositions=new ArrayList<Integer>();
    List<Integer> drawList = new ArrayList<Integer>();


    int[][] winningPositions ={{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    boolean gameActive = true;

    public  void display(View view){
        ImageView imageView = (ImageView) view;
        int position = Integer.parseInt(imageView.getTag().toString());

        Button playAgainButton = findViewById(R.id.playAgainbutton);
        TextView textView = findViewById(R.id.textView);

        if(playingPositions.get(position) == -1 && gameActive) {

            imageView.setScaleX(0);
            imageView.setScaleY(0);
            playingPositions.set(position, activePlayer);

            if (activePlayer == 0) {
                imageView.setImageResource(R.drawable.yellow);
                activePlayer = 1;
            } else {
                imageView.setImageResource(R.drawable.red);
                activePlayer = 0;
            }

            imageView.animate().scaleX(1).scaleY(1).rotation(1800).setDuration(200);

            for (int[] winPosition : winningPositions) {
                if (playingPositions.get(winPosition[0]) == playingPositions.get(winPosition[1]) && playingPositions.get(winPosition[1]) == playingPositions.get(winPosition[2])
                        && playingPositions.get(winPosition[0]) != -1 ) {

                    gameActive = false;
                    String winner;
                    if(activePlayer == 0){
                        winner = "red";
                    }else{
                        winner = "yellow";
                    }

                    textView.setText(winner +" has won");
                    playAgainButton.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);

                }
            }
        }

        Set<Integer> s = new HashSet<Integer>(playingPositions);
        if(s.size() == 2 && drawList.containsAll(s)){
            gameActive = false;
            textView.setText("It's Draw");
            playAgainButton.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
    }




    public  void PlayAgain(View view){

        Button play = findViewById(R.id.playAgainbutton);

        TextView textView = findViewById(R.id.textView);

        play.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);

        gameActive = true;

        activePlayer =0;

        android.support.v7.widget.GridLayout gridLayout = findViewById(R.id.gridLayout);
        for(int i=0;i<gridLayout.getChildCount();i++){
            playingPositions.set(i, -1);
            ImageView child = (ImageView) gridLayout.getChildAt(i);
            child.setImageDrawable(null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for(int i=0;i<9;i++){
            playingPositions.add(-1);
        }
        drawList.add(0);
        drawList.add(1);
    }
}
