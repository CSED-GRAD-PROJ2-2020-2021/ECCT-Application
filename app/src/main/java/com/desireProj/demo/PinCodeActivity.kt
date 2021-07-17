package com.desireProj.demo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.desireProj.ble_sdk.Contracts.PinCodeContract
import com.desireProj.ble_sdk.MainActivity
import com.desireProj.ble_sdk.Presenters.PinCodePresenter
import com.desireProj.ble_sdk.R
import com.desireProj.ble_sdk.model.PinCode
import com.google.android.material.textfield.TextInputEditText
import java.lang.StringBuilder

class PinCodeActivity : AppCompatActivity() ,PinCodeContract.PinCodeView{
    private var otp1: TextInputEditText? = null
    private var otp2: TextInputEditText? = null
    private var otp3: TextInputEditText? = null
    private var otp4: TextInputEditText? = null
    private var otp5: TextInputEditText? = null
    private var otp6: TextInputEditText? = null
    private lateinit var confirmButton : Button
    private var pinCode : String? = null
    private val context : Context = this
    private lateinit var pinCodePresenter: PinCodePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_code)

        val authenticationToken:String = intent.getStringExtra("authenticationToken").toString()

        pinCodePresenter = PinCodePresenter(this, context)
        val pinCode = PinCode(pinCode="1234")

        confirmButton = findViewById(R.id.buConfirm)
        confirmButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                sendAuthenticationToken(pinCode)
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

    fun sendAuthenticationToken(pinCode: PinCode){
       pinCodePresenter?.sendAuthenticationToken(pinCode)
    }

    override fun onSuccess() {
        Log.e("this","PinCode start intent")
        startActivity(Intent(this,MainActivity::class.java))
    }

    override fun onFail() {
        Toast.makeText(this,"PinError", Toast.LENGTH_SHORT).show()
    }

}