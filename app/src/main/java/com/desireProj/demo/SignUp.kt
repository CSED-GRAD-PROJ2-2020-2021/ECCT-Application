package com.desireProj.demo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.desireProj.ble_sdk.Contracts.SignUpContract
import com.desireProj.ble_sdk.Presenters.SignUpPresenter
import com.desireProj.ble_sdk.R
import com.desireProj.ble_sdk.model.PhoneNumber
import com.desireProj.ble_sdk.network.RestApiService
import com.desireProj.ble_sdk.tools.SessionManager

class SignUp : AppCompatActivity() ,SignUpContract.SignUpView{
    private lateinit var  signUpButton : Button
    private var phoneNumberText : EditText? = null
    private val context : Context = this
    private lateinit var sessionManager:SessionManager
    private lateinit var signUpPresenter: SignUpContract.SignUpPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signUpPresenter = SignUpPresenter(this,context)
        phoneNumberText = findViewById(R.id.signUpEditText)
        signUpButton = findViewById(R.id.signUpButton)
        sessionManager = SessionManager(context)
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
        /*if(sessionManager.fetchAuthToken() != "user_token"){

        }*/
    }

    fun sendPhoneNumber(phoneNumber:PhoneNumber){
      signUpPresenter?.restApiSendPhoneNumber(phoneNumber)
    }

    override fun onSuccess(authenticationToken: String?) {
        val intent = Intent(context, PinCodeActivity::class.java)
        intent.putExtra("authenticationToken",authenticationToken)
        intent.putExtra("pinCode", 1234)
        startActivity(intent)
    }

    override fun onFail() {
        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
    }


}