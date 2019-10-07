package com.example.pavan.parsedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO (2) Parse Server Connect
        Parse.initialize(new Parse.Configuration.Builder(this)
        .applicationId("965723c72c4c4a38044f304bbc2b54a0f59b15ac")
        .clientKey("e5fc87ef37f3a2e27ea8102daf00f2cee3e5ed5e")
        .server("http://104.211.200.219:80/parse/")
        .build());

        //TODO (3) Parse User permissions

        ParseACL parseACL=new ParseACL();
        parseACL.setPublicWriteAccess(true);
        parseACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(parseACL,true);



        //TODO (4) Parse Object Creation
//        ParseObject parseObject=new ParseObject("TweetObject");
//        parseObject.put("username","Kumar");
//        parseObject.put("Tweet","Yoo i like you");
//        parseObject.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e==null){
//                    Log.i("SAVE 1:","SUCCESS");
//                }else{
//                    e.printStackTrace();
//                }
//            }
//        });

        //TODO(5) updating objects and retrieving Objects
//        ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>("TweetObject");
//        parseQuery.getInBackground("plA6hiSAt6", new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject object, ParseException e) {
//                 if(object!=null){
//                     Log.i("USER",object.getString("username")+" Tweet :"+object.getString("Tweet") );
//                     object.put("Tweet","Yeah updated");
//                     object.saveInBackground();
//                 }else
//                     e.printStackTrace();
//            }
//        });

        //TODO(6) Signing Up Users
//        ParseUser parseUser=new ParseUser();
//        parseUser.setUsername("pavan");
//        parseUser.setPassword("pavankumar");
//        parseUser.signUpInBackground(new SignUpCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e==null){
//                    Log.i("SIGNED UP","SUCCESSFULLY");
//                }else
//                    Log.i("Error Message :",e.getMessage());
//            }
//        });

        //TODO(7) Logging IN and OUT of users
        if(ParseUser.getCurrentUser()!=null){
            Log.i("USER:","Hey "+ParseUser.getCurrentUser());
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        Log.i("logout","Success");
                    }else{
                        Log.i("Error Message",e.getMessage());
                    }
                }
            });
        }else{
            ParseUser.logInInBackground("pavan", "pavankumar", new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(user!=null){
                        Log.i("Login","Hey Welcome "+user.getUsername());
                    }else
                        Log.i("Error Message",e.getMessage());
                }
            });
        }








    }
}
