package com.desireProj.ble_sdk.ble

import android.util.Log
import com.desireProj.ble_sdk.model.CollectedEbid
import com.desireProj.ble_sdk.tools.Engine
import java.util.*

import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanFilter
import no.nordicsemi.android.support.v18.scanner.ScanRecord
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.android.support.v18.scanner.ScanSettings


class BleScanner{
    private var mBluetoothLeScanner: BluetoothLeScannerCompat = BluetoothLeScannerCompat.getScanner()

    private var collectedEbid: CollectedEbid? = null
    private lateinit var engine: Engine

    constructor(collectedEbid: CollectedEbid ,engine: Engine) {
        this.collectedEbid = collectedEbid
        this.engine = engine
    }
    //temprory soon will be removed (dumpy constructor)
    constructor(collectedEbid: CollectedEbid ) {
        this.collectedEbid = collectedEbid
    }


    private val mScanCallback = object : ScanCallback() {

        /*
            Callback when a BLE advertisement has been found.
            ScanResult.getScanRecord: scan record, which is a combination of advertisement and scan response.
            scanRecord.getServiceData: (byte[]) Returns the service data byte array associated with the serviceUuid.
         */
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)

            val dataReceived: ByteArray? = result.scanRecord?.serviceData?.get(BleTools.pUuid)

            if (dataReceived != null) {
                CollectedEbid.receiveEbid(dataReceived, result.rssi)
            }

            val p2Index:Byte = 0x00
            Log.e(
                "ble.onScanResult: ",
                if (dataReceived?.get(6)?.equals(p2Index)!!) "Packet 2 received" else "Packet 1 received"
            )
            var ss: String = ""
            for(c : Byte in dataReceived)
                ss+=c
            Log.e("ble.ByteArray: ",ss )
            val rssi = result.rssi
            Log.e("ble.onScanResult: ", rssi.toString())

        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("ble", "Discovery onScanFailed: $errorCode")
            super.onScanFailed(errorCode)
        }
    }

    private fun buildServiceScanSettings(): ScanSettings {
        return ScanSettings.Builder()
            .setLegacy(true)
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            .setUseHardwareFilteringIfSupported(true)
            .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
            .setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT)
            .build()
    }


    public fun startScanning() {
        val filters = ArrayList<ScanFilter>()
        val filter = ScanFilter.Builder()
            .setServiceUuid(BleTools.pUuid)
            .build()
        filters.add(filter)

        Log.e("ble.BleScanner: ", "Start Scanning")
        mBluetoothLeScanner.startScan(
            filters,
            buildServiceScanSettings(),
            mScanCallback)
    }

    public fun stopScanning() {
        mBluetoothLeScanner.stopScan(mScanCallback)
    }

}