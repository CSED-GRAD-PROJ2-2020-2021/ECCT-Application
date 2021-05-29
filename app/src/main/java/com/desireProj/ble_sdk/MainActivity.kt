package com.desireProj.ble_sdk

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.desireProj.ble_sdk.ble.BleAdvertiser
import com.desireProj.ble_sdk.ble.BleScanner
import com.desireProj.ble_sdk.database.DataBaseHandler
import com.desireProj.ble_sdk.database.PassKeyEncDec
import com.desireProj.ble_sdk.database.RTLItem
import com.desireProj.ble_sdk.model.CollectedEbid
import java.lang.StringBuilder
import com.desireProj.ble_sdk.diffieHellman.Convertor
import com.desireProj.ble_sdk.diffieHellman.KeyGenerator
import com.desireProj.ble_sdk.diffieHellman.Secret
import com.desireProj.ble_sdk.model.Utilities
import java.security.KeyPair
import java.security.SecureRandom

class MainActivity : AppCompatActivity() {
    private var mText: TextView? = null
    private var ebitText: TextView? = null
    private var mAdvertiseButton: Button? = null
    private var mDiscoverButton: Button? = null
    private var bleAdvertiser: BleAdvertiser? = null
    private var bleScanner: BleScanner? = null
    private val keyGenerator: KeyGenerator =
        KeyGenerator()
    private var privateKeyByteArray: ByteArray? = null
    private var publicKeyByteArray: ByteArray? = null
    private val convertor: Convertor =
        Convertor()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mText = findViewById(R.id.text_tv)
        ebitText = findViewById(R.id.ebid_tv)
        mDiscoverButton = findViewById(R.id.discover_btn)
        mAdvertiseButton = findViewById(R.id.advertise_btn)
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val locationPermission =
            if (Build.VERSION.SDK_INT >= 29) Manifest.permission.ACCESS_FINE_LOCATION else Manifest.permission.ACCESS_COARSE_LOCATION
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                Toast.makeText(
                    this,
                    "The permission to get BLE location data is required",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(this, "t3ala", Toast.LENGTH_SHORT).show()
                requestPermissions(arrayOf(locationPermission), 1)
            }
        } else {
            Toast.makeText(this, "Location permissions already granted", Toast.LENGTH_SHORT).show()
        }


        bleAdvertiser = BleAdvertiser()
        bleScanner = BleScanner()


        //genarate keys
        var keyPair: KeyPair = keyGenerator.generateKeyPair()
        //private and  public keys
        privateKeyByteArray = convertor.savePrivateKey(keyPair.private)
        publicKeyByteArray = convertor.savePublicKey(keyPair.public)

    }

    fun discover(view: View) {
        CollectedEbid.receivedEbidMap.clear()
        bleScanner?.startScanning()
    }

    fun advertise(view: View) {
        //advertise public byte array
//        val sendByte: ByteArray = "aabb0980b9e4dfc63d79453418b3669932bf71c5b5b06c2945ad1488744826bb72".toByteArray(Charsets.UTF_8)
//        Log.e("Main Activity: ", getEbidString(sendByte))
//        bleAdvertiser?.startAdvertising(sendByte)
//        mText!!.setText("send: "+ sendByte)
        Log.e("Main Activity: send: ", getEbidString(publicKeyByteArray!!))
        mText!!.setText("send: "+ getEbidString(publicKeyByteArray!!))
        bleAdvertiser?.startAdvertising(publicKeyByteArray!!)
    }

    fun updateMapStatus(view: View) {
        val map = CollectedEbid.receivedEbidMap
        val sb = StringBuilder()
        sb.append("sent: " + getEbidString(publicKeyByteArray!!) +"\n\n")
        sb.append("received: ")
        if (map != null) {
            for ((k, v) in map) {
                println("$k = $v")
                if (v.ebidReady) {
                    var publicSent: ByteArray? = v.ebid
                    val secret: Secret =
                        Secret(publicSent, privateKeyByteArray)
                    val secretBytes: ByteArray = secret.doECDH()
                    mText?.setText("Shared Secret: " + getEbidString(secretBytes)+"\n")

                    sb.append(v.getEbidString())
                    sb.append('\n')
                    sb.append('\n')
                }
            }
        }
        sb.append("end line")
        ebitText?.setText(sb.toString())
    }

    fun getEbidString(ebid: ByteArray):String {
        val sb = StringBuilder()
        // Iterating through each byte in the array
        for (i in ebid) {
            sb.append(String.format("%02X", i))
        }
        return sb.toString()
    }
}