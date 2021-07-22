package com.ecct.demo

import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.ecct.protocol.contracts.SignUpContract
import com.ecct.protocol.presenters.SignUpPresenter
import com.ecct.protocol.R
import com.ecct.protocol.model.PhoneNumber
import com.ecct.protocol.tools.SessionManager
import kotlin.system.exitProcess

class SignUp : AppCompatActivity() ,SignUpContract.SignUpView{
    private val phoneNumberRegex = Regex("^01[0125][0-9]{8}")
    private lateinit var  signUpButton : Button
    private var phoneNumberText : EditText? = null
    private val context : Context = this
    private lateinit var sessionManager:SessionManager
    private lateinit var signUpPresenter: SignUpContract.SignUpPresenter
    private var bluetoothAdapter : BluetoothAdapter? = null
    private var BLUETOOTH_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        openBluetooth()

        signUpPresenter = SignUpPresenter(this,context)
        phoneNumberText = findViewById(R.id.signUpEditText)
        signUpButton = findViewById(R.id.signUpButton)
        sessionManager = SessionManager(context)
        if(sessionManager.fetchAuthToken()!=null){
            startActivity(Intent(context, MainActivity::class.java))
        }
        signUpButton.setOnClickListener {
            if (phoneNumberText?.text.toString().contains(phoneNumberRegex)) {
                val phoneNumber = PhoneNumber(phoneNumber = phoneNumberText?.text.toString())
                sendPhoneNumber(phoneNumber)
            } else {
                showDialog("Alert", "Enter valid phone number", "OK")
                Toast.makeText(context, "Please enter valid phone number", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        /*if(sessionManager.fetchAuthToken() != "user_token"){
!(phoneNumberText?.text.toString().isEmpty() ||
                            (phoneNumberText?.text.toString().length < 11) ||
                            !phoneNumberText?.text.toString().isDigitsOnly())
        }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == BLUETOOTH_CODE){
            if(resultCode != RESULT_OK){
                moveTaskToBack(true)
                exitProcess(1)
            }
        }
    }

    private fun sendPhoneNumber(phoneNumber:PhoneNumber){
      signUpPresenter.sendPhoneNumber(phoneNumber)
    }

    private fun openBluetooth(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null){
            Toast.makeText(applicationContext,"Bluetooth Not Supported",Toast.LENGTH_SHORT).show()
        }else{
            if(!bluetoothAdapter!!.isEnabled){
                startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),BLUETOOTH_CODE)
                Toast.makeText(applicationContext,"Bluetooth Turned ON",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun showDialog(title : String, body : String, dismissText:String){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialog_view)
        val bodyText = dialog.findViewById<TextView>(R.id.tvBody)
        bodyText.text = body
        //dialog.setM
        val titleText = dialog.findViewById<TextView>(R.id.tvTitle)
        titleText.text = title

        val yesButton = dialog.findViewById<Button>(R.id.btn_yes)
        yesButton.text = dismissText
        yesButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                dialog.dismiss()
            }

        })
        dialog.show()


       /* val builder : AlertDialog.Builder = context.let {
            AlertDialog.Builder(it)
        }


        builder.setMessage("Enter valid phone number!")
        builder.setTitle("Invalid Phone Number")
        builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })

        val alertDialog : AlertDialog = builder.create()
        alertDialog.show()*/
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