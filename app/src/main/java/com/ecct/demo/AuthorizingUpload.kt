package com.ecct.demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.ecct.protocol.R
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONObject

class AuthorizingUpload : AppCompatActivity() {
    private lateinit var explianTextView : TextView
    private lateinit var scanButton : Button
    private lateinit var uploadButton : Button
    private lateinit var qrScanIntegrator : IntentIntegrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorizing_upload)

        explianTextView = findViewById(R.id.tvExplain)
        scanButton = findViewById(R.id.btnScan)
        uploadButton = findViewById(R.id.btnUpload)

        scanButton.setOnClickListener { object : View.OnClickListener{
            override fun onClick(v: View?) {
                qrScanIntegrator.initiateScan()
            }

        } }


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
                explianTextView?.text = "Click on Upload Button\n" + result.contents.toString()
                uploadButton.visibility = View.VISIBLE
                scanButton.visibility = View.GONE
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}