package com.desireProj.demo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.desireProj.ble_sdk.MainActivity
import com.desireProj.ble_sdk.R

class SignUp : AppCompatActivity() {
    private lateinit var  signUpButton : Button
    private val context : Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        signUpButton = findViewById(R.id.signUpButton)
        signUpButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(context , MainActivity::class.java)
                startActivity(intent)
            }
        })
    }

}