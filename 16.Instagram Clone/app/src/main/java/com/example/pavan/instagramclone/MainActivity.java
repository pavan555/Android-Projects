package com.example.pavan.instagramclone;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity{

    TextView textButton,username,password;
    static Toast toast;
    Button button;


    public void showUsers(){
        Intent intent=new Intent(MainActivity.this,UserListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.signup);
        textButton=findViewById(R.id.loginSwap);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        ConstraintLayout constraintLayout=findViewById(R.id.contraintLayout);

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
            }
        });



        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    hideKeyBoard();
                    SignUpOrLogIn(v);
                    return true;
            }
        });


        Parse.enableLocalDatastore(this);
        //pavankumar.southindia.cloudapp.azure.com
        Parse.initialize(new Parse.Configuration.Builder(this)
        .applicationId("965723c72c4c4a38044f304bbc2b54a0f59b15ac")
        .clientKey("e5fc87ef37f3a2e27ea8102daf00f2cee3e5ed5e")
        .server("http://pavankumar.southindia.cloudapp.azure.com/parse")
        .build());

        if(ParseUser.getCurrentUser()!=null) showUsers();

    }

    public void hideKeyBoard(){
        InputMethodManager inputMethodManager=(InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }




    public static void showToast(String message,Context context){
        if(toast!=null)
            toast.cancel();
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public void swap(View view){
        if((button.getText()).equals(getResources().getString(R.string.signup))){
            button.setText(R.string.login);
            textButton.setText(R.string.signup);
        }else{
            textButton.setText(R.string.login);
            button.setText(R.string.signup);
        }
        
    }



    public void SignUpOrLogIn(View view) {

        if(ParseUser.getCurrentUser()==null){
              if(button.getText().equals(getResources().getString(R.string.signup))){
                  ParseUser parseUser=new ParseUser();
                  parseUser.setUsername(username.getText().toString());
                  parseUser.setPassword(password.getText().toString());

                  parseUser.signUpInBackground(new SignUpCallback() {
                      @Override
                      public void done(ParseException e) {
                          if(e==null) {
                              Log.i("SIGN UP", "Signed Up Successfully");
                              showToast("Signed Up Successfully",MainActivity.this);
                              showUsers();
                          }
                          else {
                              String message=e.getMessage();
                              if(message.length()>70) message = message.substring(36);
                              Log.i("EXCEPTION", message);
                              showToast(message,MainActivity.this);
                          }
                      }

                  });
              }else{
                  ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                      @Override
                      public void done(ParseUser user, ParseException e) {
                          if(user!=null) {
                              Log.i("USER LOGIN","Hey Welcome Again "+user.getUsername());
                              showToast("Hey Welcome Again "+user.getUsername(),MainActivity.this);
                              showUsers();
                          }else showToast(e.getMessage(),MainActivity.this);
                      }
                  });
              }

        }else {
            showToast("Already Logged in "+ParseUser.getCurrentUser().getUsername(),MainActivity.this);
        }

    }



}
