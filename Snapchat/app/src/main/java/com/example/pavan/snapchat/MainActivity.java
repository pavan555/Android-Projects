package com.example.pavan.snapchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button signUp,logIn;
    EditText mEmail,mPassword;
    ImageView imageView;
    FirebaseAuth firebaseAuth;
    private String password="no Password";
    private String email="sorry no email";
    private boolean toggle=false;
    private boolean isLogIn=false,isSignUp=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signUp = findViewById(R.id.signUp);
        logIn = findViewById(R.id.logIn);
        imageView = findViewById(R.id.snapPic);
        mEmail=findViewById(R.id.email);
        ConstraintLayout constraintLayout=findViewById(R.id.constriantLayout);
        mPassword = findViewById(R.id.password);
        firebaseAuth=FirebaseAuth.getInstance();


        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                hideKeyBoard(MainActivity.this);

                if(!isLogIn) goLogin(v);
                if(!isSignUp) goSignUp(v);
                return true;
            }
        });



        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard(MainActivity.this);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser() != null){
            Log.w("User alert","Hi "+firebaseAuth.getCurrentUser().getEmail());
            moveToNext();
        }
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }



    public static void hideKeyBoard(Activity activity){
        InputMethodManager inputMethodManager=(InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View focus = activity.getCurrentFocus();
        if(focus !=null)
                inputMethodManager.hideSoftInputFromWindow(focus.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }




    public String check(){
        String error = "";
        if(mEmail.length() == 0)
            error="please enter email";
        if(mPassword.length() == 0) {
            if(error.length() > 0 )
                error+=",password";
            else
                error="please enter password";
        }
        return error;
    }



    public void goLogin(View view) {



        if(!toggle && !isSignUp || isLogIn){
            //signUp.setVisibility(View.INVISIBLE);
            isSignUp=true;
            isLogIn=false;
            imageView.animate().y(100).scaleX(0.5f).scaleY(0.5f).setDuration(1200).start();
            mEmail.setVisibility(View.VISIBLE);
            mPassword.setVisibility(View.VISIBLE);

            //TODO(2) Animate Dynamically According to the Device Screen Width and Heigh

            logIn.animate().scaleX(0.5f).scaleY(0.7f).y(mPassword.getY()+80+10).setDuration(500).start();
            signUp.animate().scaleX(0.5f).scaleY(0.5f).y(mPassword.getY()+80+225).setDuration(500).start();


        }else {
            toggle = true;
            String error = check();
            if (error.length() > 0) {
                Toast.makeText(MainActivity.this,error,
                        Toast.LENGTH_SHORT).show();
            } else {
                email = mEmail.getText().toString();
                password = mPassword.getText().toString();

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            moveToNext();
                        }

                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }
    }


    public void goSignUp(View view) {


        if(!toggle && !isLogIn || isSignUp){
            //logIn.setVisibility(View.INVISIBLE);
            isLogIn=true;
            isSignUp=false;
            imageView.animate().y(100).scaleX(0.5f).scaleY(0.5f).setDuration(1200).start();
            mEmail.setVisibility(View.VISIBLE);
            mPassword.setVisibility(View.VISIBLE);

            signUp.animate().scaleX(0.5f).scaleY(0.7f).y(mPassword.getY()+80+10).setDuration(500).start();
            //80 is height of Password field and it works only if it is invisible not gone
            logIn.animate().scaleX(0.5f).scaleY(0.5f).y(mPassword.getY()+80+225).setDuration(500).start();
        }else{

            toggle=true;
            String error = check();
            if (error.length() > 0) {
                Toast.makeText(MainActivity.this,error,
                        Toast.LENGTH_SHORT).show();
            } else {
                email = String.valueOf(mEmail.getText());
                password = String.valueOf(mPassword.getText());

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                                FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).child("email").setValue(email);
                            //move to next Activity
                                moveToNext();
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }


    }
    public void moveToNext(){
        Intent intent=new Intent(this,SecondActivity.class);
        startActivity(intent);
    }
}
