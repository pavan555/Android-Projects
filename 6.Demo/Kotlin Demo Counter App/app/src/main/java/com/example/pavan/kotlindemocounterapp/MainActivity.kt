package com.example.pavan.kotlindemocounterapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var textView: TextView
    var count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
    }

    fun increase(v: View) {
        count += 1
        textView.text = count.toString()
    }

    fun decrease(v: View) {
        if (count > 0) count -= 1 else count = 0
        textView.text = count.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.reset) {
            count = 0
            textView.text = count.toString()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
