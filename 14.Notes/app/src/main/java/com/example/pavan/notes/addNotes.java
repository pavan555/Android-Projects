package com.example.pavan.notes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

public class addNotes extends AppCompatActivity {


    public void goBack(View view) {

        finish();
    }


    Integer position;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        editText = findViewById(R.id.editText);
        String notes = getIntent().getStringExtra("notes");
        position = getIntent().getIntExtra("position", -1);

        editText.setText(notes);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (MainActivity.stringArrayList.size() > 0 && position != -1) {
                    MainActivity.stringArrayList.set(position, String.valueOf(s));
                } else {
                    MainActivity.stringArrayList.add(String.valueOf(s));
                    position = MainActivity.stringArrayList.size() - 1;
                }
                MainActivity.arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    @Override
    protected void onDestroy() {


        if (position != -1 && MainActivity.stringArrayList.get(position).isEmpty()) {
//            Log.i("POs",position.toString());
            MainActivity.stringArrayList.remove(MainActivity.stringArrayList.get(position));
            MainActivity.arrayAdapter.notifyDataSetChanged();
        }
        try {
            getSharedPreferences("com.example.pavan.notes", MODE_PRIVATE).edit().putString("notess", ObjectSerializer.serialize(MainActivity.stringArrayList)).apply();

        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
