package com.example.pavan.snapchat

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.view.isVisible

class MainActivity_1 : AppCompatActivity() {

    private lateinit var signUp:Button
    private lateinit var logIn:Button

    var email:EditText?=null
    var password:EditText?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        signUp = findViewById(R.id.signUp)
        logIn = findViewById(R.id.logIn)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }



    fun goLogin(view: View){
        if(signUp.isVisible){
            signUp.visibility=View.INVISIBLE
        }else{


        }
    }
}
