package com.ecct.demo

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.ecct.protocol.contracts.UploadContract
import com.ecct.protocol.presenters.UploadPresenter
import com.ecct.protocol.R
import com.ecct.protocol.model.UploadPetsModel
import com.ecct.protocol.tools.Engine
import com.google.zxing.integration.android.IntentIntegrator

class UploadActivity:AppCompatActivity(),UploadContract.UploadView {
    private lateinit var  uploadResultButton : Button
    private lateinit var scanButton : Button
    private var uploadText : TextView? = null
    private val context : Context = this
    private lateinit var qrScanIntegrator : IntentIntegrator
    private lateinit var uploadPresenter: UploadPresenter
    private lateinit var engine: Engine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload_activity)
        uploadText = findViewById(R.id.tvExplain)
        scanButton = findViewById(R.id.btnScan)
        uploadResultButton = findViewById(R.id.btnUpload)
        uploadPresenter = UploadPresenter(this,context)
        engine =Engine
        qrScanIntegrator = IntentIntegrator(this)

        uploadResultButton.setOnClickListener(object : View.OnClickListener{
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onClick(v: View?) {
                val uploadPet  = UploadPetsModel("T2",engine.getETLList(engine.currentDate()))
                uploadPresenter.uploadPets(uploadPet)
            }
        })
        scanButton.setOnClickListener ( object : View.OnClickListener{
            override fun onClick(v: View?) {
                qrScanIntegrator.initiateScan()
            }

        } )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            // If QRCode has no data.
            if (result.contents == null) {
                Toast.makeText(this, getString(R.string.qr_empty), Toast.LENGTH_LONG).show()
            } else {
                // If QRCode contains data.
                // Show values in UI.
                vibrate()
                uploadText?.text = "Click on Upload Button\n" + result.contents.toString()
                uploadResultButton.visibility = View.VISIBLE
                scanButton.visibility = View.GONE
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSuccess() {
        uploadText?.setText("uploaded")
        startActivity(Intent(context, MainActivity::class.java))
    }
    fun Context.vibrate(){
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) { // Vibrator availability checking
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)) // New vibrate method for API Level 26 or higher
            } else {
                vibrator.vibrate(500) // Vibrate method for below API Level 26
            }
        }
    }
}