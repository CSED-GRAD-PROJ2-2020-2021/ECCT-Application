package com.desireProj.ble_sdk

import android.Manifest
import android.content.Intent
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
import com.desireProj.ble_sdk.model.CollectedEbid

import com.desireProj.ble_sdk.model.StoredPET
import com.desireProj.ble_sdk.model.StoredPETsModel
import com.desireProj.ble_sdk.model.UploadedPetsModel
import com.desireProj.ble_sdk.network.RestApiService
import java.lang.StringBuilder
import com.desireProj.ble_sdk.diffieHellman.Convertor
import com.desireProj.ble_sdk.diffieHellman.KeyGenerator
import com.desireProj.ble_sdk.diffieHellman.Secret
import com.desireProj.ble_sdk.tools.*
import java.security.KeyPair
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private var mText: TextView? = null
    private var ebitText: TextView? = null
    private var mAdvertiseButton: Button? = null
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private var mDiscoverButton: Button? = null
    private var bleAdvertiser: BleAdvertiser? = null
    private var bleScanner: BleScanner? = null
    private val keyGenerator: KeyGenerator =
        KeyGenerator()
    private var privateKeyByteArray: ByteArray? = null
    private var publicKeyByteArray: ByteArray? = null
    private val convertor: Convertor =
        Convertor()

    private var collectedEbid: CollectedEbid? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mText = findViewById(R.id.text_tv)
        ebitText = findViewById(R.id.ebid_tv)
        mDiscoverButton = findViewById(R.id.discover_btn)
        mAdvertiseButton = findViewById(R.id.advertise_btn)
        startButton = findViewById(R.id.start_btn)
        stopButton = findViewById(R.id.stop_btn)
        startButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                actionOnService(Actions.START)
            }})
        stopButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                actionOnService(Actions.STOP)
            }})


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


        collectedEbid = CollectedEbid()


        bleAdvertiser = BleAdvertiser()
        bleScanner = BleScanner()


        //genarate keys
        var keyPair: KeyPair = keyGenerator.generateKeyPair()
        //private and  public keys
        privateKeyByteArray = convertor.savePrivateKey(keyPair.private)
        publicKeyByteArray = convertor.savePublicKey(keyPair.public)
    }

    fun discover(view: View) {
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
        val sb = StringBuilder("received: ")
        if (map != null) {
            for ((k, v) in map) {
                println("$k = $v")
                if (v.ebidReady) {
                    var publicSent: ByteArray? = v.ebid
                    val secret: Secret =
                        Secret(publicSent, privateKeyByteArray)
                    val secretBytes: ByteArray = secret.doECDH()
                    mText?.setText("secret: " + getEbidString(secretBytes)+"\n")

                    sb.append(v.getEbidString())
                    sb.append('\n')
                    sb.append('\n')
                }
            }
        }
        sb.append("end line bla bla")
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
    fun query(view: View) {
        val apiService = RestApiService()
        val list = ArrayList<StoredPET>()
        val p =StoredPET(PETID = "253",
            RSSI = -60,
            duration = 12.53,
            meetingDate = Date().time
        )
        list.add(p)
        val pet = StoredPETsModel(  key ="2468688658",
            id = "123",
            pets = list
            )

        apiService.queryPets(pet) {
            if (it?.status != null) {
                // it = newly added user parsed as response
                // it?.id = newly added user ID
            } else {
                Log.e("here","Error registering new user")
            }
        }
    }
    fun upload(view: View){
        val apiService = RestApiService()
        val list = ArrayList<String>()

        list.add("15686a")
        val pet = UploadedPetsModel(  key ="2468688658",
            id = "123",
            pets = list
        )

        apiService.uploadPets(pet) {
            if (it?.status != null) {
                // it = newly added user parsed as response
                // it?.id = newly added user ID
            } else {
                Log.e("here","Error registering new user")
            }
        }
    }
    private fun actionOnService(action: Actions) {
        if (getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP) return
        Intent(this, BleForgroundService::class.java).also {
            it.action = action.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                log("Starting the service in >=26 Mode")
                startForegroundService(it)
                return
            }
            log("Starting the service in < 26 Mode")
            startService(it)
        }
    }

}