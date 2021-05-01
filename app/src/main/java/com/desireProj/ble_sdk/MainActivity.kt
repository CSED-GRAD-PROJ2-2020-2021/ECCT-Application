package com.desireProj.ble_sdk

import android.Manifest
import android.bluetooth.BluetoothAdapter
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
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat

class MainActivity : AppCompatActivity() {
    private var mText: TextView? = null
    private var mAdvertiseButton: Button? = null
    private var mDiscoverButton: Button? = null
    private var bleAdvertiser: BleAdvertiser? = null
    private var bleScanner: BleScanner? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mText = findViewById(R.id.text_tv)
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

    }

    fun discover(view: View) {
        bleScanner?.startScanning()
    }

    fun advertise(view: View) {
        bleAdvertiser?.startAdvertising("/A%D*G-KaPdSgVkYp3s6v9y\$B&E(H+Mb")
    }
}