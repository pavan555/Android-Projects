package com.example.pavan.weatherapp;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pavan.weatherapp.utils.NetworkUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button submit;
    TextView textView;
    ConstraintLayout constraintLayout;
    ProgressBar progressBar;
    String city;

    public void hideKeyBoard(){
        InputMethodManager inputManager =
                (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }
    public void submit(View view){
        hideKeyBoard();
        city = editText.getText().toString();
        URL url =NetworkUtils.buildURL(city);

        try {
            JSONObject json = new getWeather().execute(url).get();
            textView.setText(NetworkUtils.JsonBuilder(json));
        } catch (Exception e) {
            e.printStackTrace();
        }
        constraintLayout.setVisibility(View.VISIBLE);
    }



    public class getWeather extends AsyncTask<URL,Void, JSONObject>{
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(URL... urls) {
            URL url = urls[0];
            JSONObject result = new JSONObject();
            try {
                result = NetworkUtils.getResponseFromHttpConnection(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject s) {
            progressBar.setVisibility(View.INVISIBLE);
            if (s == null || s.length() == 0) {
                String html = "<font color=\"#dc3545\">No Internet Connection !! or Enter Correct Name</font>";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
                    // we are using this flag to give a consistent behaviour
                    textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
                } else {
                    textView.setText(Html.fromHtml(html));
                }
            }
            super.onPostExecute(s);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.city);
        submit = findViewById(R.id.button);
        constraintLayout = findViewById(R.id.data);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.weatherData);

        //enterning text is done ,if user clicks enter then it will be submitted by this method
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    submit.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


}
