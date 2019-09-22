package com.example.pavan.alertandmenudemo;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    String[] lang={"English","Telugu"};
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=new MenuInflater(this);
        menuInflater.inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.tv);
        final SharedPreferences sharedPreferences=this.getSharedPreferences("com.example.pavan.alertandmenudemo",MODE_PRIVATE);
        try{
            String language = sharedPreferences.getString("language", "");
            if(language.isEmpty()){
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("Update Language")
                        .setMessage("Choose the Language")
                        .setNegativeButton("Telugu", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                which=Math.abs(which)-1;
                                sharedPreferences.edit().putString("language", lang[which]).apply();
                                textView.setText(lang[which].toUpperCase());
                                Toast.makeText(MainActivity.this,"Saved!!",Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setPositiveButton("English", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                which = Math.abs(which) - 1;
                                sharedPreferences.edit().putString("language", lang[which]).apply();
                                textView.setText(lang[which].toUpperCase());
                                Toast.makeText(MainActivity.this,"Saved!!",Toast.LENGTH_SHORT).show();

                            }
                        })
                        .show();
            }else{
                textView.setText(language);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.exit:
                finish();
                return true;
            case R.id.update:
                final String language=getSharedPreferences("com.example.pavan.alertandmenudemo",MODE_PRIVATE).getString("language", "");
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("Update Language")
                        .setMessage("Choose the Language")
                        .setPositiveButton("English", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                which=Math.abs(which)-1;
                                if(language.equals(lang[which])){
                                    new AlertDialog.Builder(MainActivity.this)
                                            .setIcon(android.R.drawable.ic_input_add)
                                            .setTitle("Update")
                                            .setMessage("You Selected Same Language").show();
                                }else{
                                    getSharedPreferences("com.example.pavan.alertandmenudemo",MODE_PRIVATE).edit().putString("language", lang[which]).apply();
                                    Toast.makeText(MainActivity.this,"Updated!!",Toast.LENGTH_SHORT).show();
                                    textView.setText(lang[which]);

                                }
                            }
                        })
                        .setNegativeButton("Telugu", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                which=Math.abs(which)-1;
                                Log.i("WHICH", String.valueOf(which));
                                if(language.equals(lang[which])){
                                    new AlertDialog.Builder(MainActivity.this)
                                            .setIcon(android.R.drawable.ic_input_add)
                                            .setTitle("Update")
                                            .setMessage("You Selected Same Language").show();
                                }else{
                                    getSharedPreferences("com.example.pavan.alertandmenudemo",MODE_PRIVATE).edit().putString("language", lang[which]).apply();
                                    Toast.makeText(MainActivity.this,"Updated!!",Toast.LENGTH_SHORT).show();
                                    textView.setText(lang[which]);

                                }
                            }
                        })
                        .show();
                return true;
            default:
                return false;
        }
    }
}
