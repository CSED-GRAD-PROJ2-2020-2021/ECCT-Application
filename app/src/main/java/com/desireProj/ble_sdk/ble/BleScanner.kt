package com.desireProj.ble_sdk.ble

import android.util.Log
import com.desireProj.ble_sdk.model.CollectedEbid
import java.util.*

import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanFilter
import no.nordicsemi.android.support.v18.scanner.ScanRecord
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.android.support.v18.scanner.ScanSettings


class BleScanner{
    private val bleTools: BleTools = BleTools()
    private var mBluetoothLeScanner: BluetoothLeScannerCompat = BluetoothLeScannerCompat.getScanner()

    private var collectedEbid: CollectedEbid? = null

    constructor(collectedEbid: CollectedEbid) {
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

            val dataReceived: ByteArray? = result.scanRecord?.serviceData?.get(bleTools.pUuid)

            if (dataReceived != null) {
                collectedEbid?.receiveEbid(dataReceived)
            }

            Log.e(
                "ble.onScanResult: ",
                if (dataReceived?.get(6)?.equals(0x01)!!) "Packet 1 received" else "Packet 2 received"
            )

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
            .setServiceUuid(bleTools.pUuid)
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