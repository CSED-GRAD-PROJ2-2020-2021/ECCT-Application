package com.desireProj.demo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.desireProj.ble_sdk.R
import com.desireProj.ble_sdk.model.AuthenticationToken
import com.desireProj.ble_sdk.model.AuthenticationTokenResponse
import com.desireProj.ble_sdk.model.PhoneNumber
import com.desireProj.ble_sdk.network.RestApiService
import com.google.android.material.textfield.TextInputEditText
import java.lang.StringBuilder

class PinCodeActivity : AppCompatActivity() {
    private var otp1: TextInputEditText? = null
    private var otp2: TextInputEditText? = null
    private var otp3: TextInputEditText? = null
    private var otp4: TextInputEditText? = null
    private var otp5: TextInputEditText? = null
    private var otp6: TextInputEditText? = null
    private lateinit var confirmButton : Button
    private var pinCode : String? = null
    private val context : Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_code)

        val authenticationToken:String = intent.getStringExtra("authenticationToken").toString()
        val pinCode:String = intent.getStringExtra("pinCode").toString()

        val authenticationTokenObject = AuthenticationToken(authenticationToken = authenticationToken, pinCode = pinCode, error = "")

        confirmButton = findViewById(R.id.buConfirm)
        confirmButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                sendAuthenticationToken(authenticationTokenObject)
            }
        })


        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);
        OTPButtonsFocus();
    }

    private fun OTPButtonsFocus() {
        OTPButtonListener(otp1, otp2);
        OTPButtonListener(otp2, otp3);
        OTPButtonListener(otp3, otp4);
        OTPButtonListener(otp4, otp5);
        OTPButtonListener(otp5, otp6);
    }

    private fun OTPButtonListener(otp1: TextInputEditText?, otp2: TextInputEditText?) {

        val textWatcher = object : TextWatcher {
            var stringBuilder : StringBuilder? = null
            override fun afterTextChanged(s: Editable?) {
                if(stringBuilder?.length == 0){
                    otp1?.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if(stringBuilder?.length == 1){
                    stringBuilder?.deleteCharAt(0)
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                stringBuilder?.append(s)
                if (otp1?.text?.length == 1 && stringBuilder?.length == 0) {
                    otp1?.clearFocus()
                    otp2?.requestFocus()
                    otp2?.isCursorVisible = true
                }
            }
        }
        otp1?.addTextChangedListener(textWatcher)
    }

    fun sendAuthenticationToken(authenticationToken: AuthenticationToken){
        val apiService = RestApiService()

        apiService.sendAuthenticationToken(authenticationToken){
            Toast.makeText(context, it?.hashedPhoneNumber, Toast.LENGTH_SHORT).show()
            Toast.makeText(context, it?.key, Toast.LENGTH_SHORT).show()
            Toast.makeText(context, it?.iv, Toast.LENGTH_SHORT).show()
        }
    }

}