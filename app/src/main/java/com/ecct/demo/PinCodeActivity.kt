/**
 * Author: Mohamed Samy
 */
package com.ecct.demo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.ecct.protocol.contracts.PinCodeContract
import com.ecct.protocol.presenters.PinCodePresenter
import com.ecct.protocol.R
import com.ecct.protocol.model.PinCode
import com.google.android.material.textfield.TextInputEditText
import java.lang.StringBuilder

class PinCodeActivity : AppCompatActivity() ,PinCodeContract.PinCodeView{
    private var otp1: TextInputEditText? = null
    private var otp2: TextInputEditText? = null
    private var otp3: TextInputEditText? = null
    private var otp4: TextInputEditText? = null

    private lateinit var confirmButton : Button
    private val context : Context = this
    private lateinit var pinCodePresenter: PinCodePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_code)

        val authenticationToken:String = intent.getStringExtra("authenticationToken").toString()

        pinCodePresenter = PinCodePresenter(this, context)
        val pinCode = PinCode(pinCode="1234")

        otp1 = findViewById(R.id.otp1)
        otp2 = findViewById(R.id.otp2)
        otp3 = findViewById(R.id.otp3)
        otp4 = findViewById(R.id.otp4)

        OTPButtonsFocus()

        confirmButton = findViewById(R.id.buConfirm)
        confirmButton.setOnClickListener {
            pinCode.pinCode =
                otp1.toString() + otp2.toString() + otp3.toString() + otp4.toString()
            if (pinCode.pinCode.isEmpty() || pinCode.pinCode.length < 4) {

            } else {
                sendAuthenticationToken(pinCode)
            }
        }
    }

    private fun OTPButtonsFocus() {
        OTPButtonListener(otp1, otp2)
        OTPButtonListener(otp2, otp3)
        OTPButtonListener(otp3, otp4)
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
                    otp1.clearFocus()
                    otp2?.requestFocus()
                    otp2?.isCursorVisible = true
                }
            }
        }
        otp1?.addTextChangedListener(textWatcher)
    }

    fun sendAuthenticationToken(pinCode: PinCode){
       pinCodePresenter.sendAuthenticationToken(pinCode)
    }

    override fun onSuccess() {
        Log.e("this","PinCode start intent")
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onFail() {
        Toast.makeText(this,"PinError", Toast.LENGTH_SHORT).show()
    }

}