package com.example.pavan.kotlindemocounterapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
    }

    fun increase(v: View) {
        textView.text = ((textView.text).toString().toInt() + 1).toString()
    }

    fun decrease(v: View) {
        if (textView.text.toString().toInt() > 0) textView.text = ((textView.text).toString().toInt() - 1).toString()
        else textView.text = "0"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.reset) {
            textView.text = "0"
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
