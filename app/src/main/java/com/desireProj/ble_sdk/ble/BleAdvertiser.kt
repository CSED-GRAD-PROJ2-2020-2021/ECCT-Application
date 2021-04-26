package com.desireProj.ble_sdk.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.util.Log
import com.desireProj.ble_sdk.model.EbidPacket

class BleAdvertiser{

    private var bleAdvertiser: BluetoothLeAdvertiser = BluetoothAdapter.getDefaultAdapter().bluetoothLeAdvertiser
    private val bleTools: BleTools = BleTools()

    private val advertisingCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
            Log.e("ble.BleAdvertiser: ", "Advertising success")
            super.onStartSuccess(settingsInEffect)
        }

        override fun onStartFailure(errorCode: Int) {
            Log.e("ble.BleAdvertiser: ", "Advertising onStartFailure: $errorCode")
            super.onStartFailure(errorCode)
        }
    }

    fun preparePackets(ebid: String): EbidPacket{
        var ebidPacket = EbidPacket(ebid)
        ebidPacket.generateEbidPacket()
        return ebidPacket
    }

    private fun buildAdvertiserSettings(): AdvertiseSettings {
        return AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setConnectable(false)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
            .setTimeout(0)
            .build()
    }

    private fun buildAdvertiseData(data: ByteArray): AdvertiseData {
        return AdvertiseData.Builder()
            .addServiceUuid(bleTools.pUuid)
            .addServiceData(bleTools.pUuid, data)
            .setIncludeDeviceName(false)
            .setIncludeTxPowerLevel(false)
            .build()
    }

    /*
        advertising ebit on two packets, advertise packet 1 and wait for 2 seconds,
        then advertise packet 2 for 2 seconds
     */
    fun startAdvertising(ebid: String) {
        var ebidPacket = preparePackets(ebid)
        var ebidLsbData = buildAdvertiseData(ebidPacket.packet1)
        var ebidMsbData = buildAdvertiseData(ebidPacket.packet2)

        // advertising packet 1
        bleAdvertiser.startAdvertising(
            buildAdvertiserSettings(),
            ebidLsbData,
            advertisingCallback)
        Log.e("ble.BleAdvertiser: ", "Advertising packet 1")
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        bleAdvertiser.stopAdvertising(advertisingCallback)



        // advertising packet 2
        bleAdvertiser.startAdvertising(
            buildAdvertiserSettings(),
            ebidMsbData,
            advertisingCallback)
        Log.e("ble.BleAdvertiser: ", "Advertising packet 2")
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        bleAdvertiser.stopAdvertising(advertisingCallback)

    }

    fun stopAdvertising() {
        bleAdvertiser.stopAdvertising(advertisingCallback)
    }

}