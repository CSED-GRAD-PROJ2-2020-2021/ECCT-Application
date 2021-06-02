package com.desireProj.demo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.desireProj.ble_sdk.R
import com.desireProj.ble_sdk.model.PhoneNumber
import com.desireProj.ble_sdk.network.RestApiService

class SignUp : AppCompatActivity() {
    private lateinit var  signUpButton : Button
    private var phoneNumberText : EditText? = null
    private val context : Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        phoneNumberText = findViewById(R.id.signUpEditText)
        signUpButton = findViewById(R.id.signUpButton)
        signUpButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if(!phoneNumberText?.text.toString().isEmpty()) {
                    var phoneNumber = PhoneNumber(phoneNumber = phoneNumberText?.text.toString())
                    sendPhoneNumber(phoneNumber)
                }else{
                    Toast.makeText(context, "Please enter valid phone number", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun sendPhoneNumber(phoneNumber:PhoneNumber){
        val apiService = RestApiService()

        apiService.sendPhoneNumber(phoneNumber){
            if(it?.authenticationToken != null && it?.pinCode != null){
                val intent = Intent(context, PinCodeActivity::class.java)
                intent.putExtra("authenticationToken",it.authenticationToken)
                intent.putExtra("pinCode", it.pinCode)
                startActivity(intent)
            }else{
                Toast.makeText(context, it?.error, Toast.LENGTH_SHORT).show()
            }
        }
    }

}