package com.desireProj.ble_sdk.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.util.Log
import com.desireProj.ble_sdk.model.EbidPacket

private const val ADVERTISE_INTERVAL: Long = 3000
private const val REST_INTERVAL: Long = 1000

class BleAdvertiser{

    private var bleAdvertiser: BluetoothLeAdvertiser = BluetoothAdapter.getDefaultAdapter().bluetoothLeAdvertiser

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

    fun preparePackets(ebid: ByteArray): EbidPacket{
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
            .addServiceUuid(BleTools.pUuid)
            .addServiceData(BleTools.pUuid, data)
            .setIncludeDeviceName(false)
            .setIncludeTxPowerLevel(false)
            .build()
    }

    /*
        advertising ebit on two packets, advertise packet 1 and wait for 1 seconds,
        then advertise packet 2 for 1 seconds

        the whole function takes 16 seconds
     */
    public fun startAdvertising(ebid: ByteArray) {
        var ebidPacket = preparePackets(ebid)
        var ebidLsbData = buildAdvertiseData(ebidPacket.packet1)
        var ebidMsbData = buildAdvertiseData(ebidPacket.packet2)

        sendBothPackets(ebidLsbData, ebidMsbData)   // takes 8 seconds

        sendBothPackets(ebidLsbData, ebidMsbData)   // takes 8 seconds

    }

    private fun sendPacket(packetData: AdvertiseData, packetNo: Int) {
        bleAdvertiser.startAdvertising(
            buildAdvertiserSettings(),
            packetData,
            advertisingCallback)
        Log.e("ble.BleAdvertiser: ", "Advertising packet $packetNo")
        try {
            Thread.sleep(ADVERTISE_INTERVAL)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        bleAdvertiser.stopAdvertising(advertisingCallback)

        Thread.sleep(REST_INTERVAL)
    }

    private fun sendBothPackets(ebidLsbData: AdvertiseData, ebidMsbData: AdvertiseData) {
        // advertising packet 1
        sendPacket(ebidLsbData, 1) // takes 4 seconds

        // advertising packet 2
        sendPacket(ebidMsbData, 2) // takes 4 seconds
    }

    public fun stopAdvertising() {
        bleAdvertiser.stopAdvertising(advertisingCallback)
    }

}