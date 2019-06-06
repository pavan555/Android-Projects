package com.example.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final Double exchangeValue = 69.38;
    public void changeCurrency(View view){
        EditText dollarTv = findViewById(R.id.dollarTv);

        String rupee = getResources().getString(R.string.rupee);
        String dollar = getResources().getString(R.string.dollar);
        TextView rupeeTv = findViewById(R.id.rupeeTv);
        Double rupees = Double.parseDouble(dollarTv.getText().toString()) * exchangeValue;
        String rupeesValue = String.format("%.2f",rupees);
        rupeeTv.setText(String.format("%s%s Will be %s%s",dollar,dollarTv.getText().toString(), rupee, rupeesValue));
//        Toast.makeText(this,rupee+dollarTv.getText().toString(),Toast.LENGTH_SHORT).show();
        rupeeTv.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
