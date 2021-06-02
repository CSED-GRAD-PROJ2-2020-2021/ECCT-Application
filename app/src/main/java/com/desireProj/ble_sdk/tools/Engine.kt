package com.desireProj.ble_sdk.tools

import android.util.Log
import com.desireProj.ble_sdk.Contracts.LoggerContract
import com.desireProj.ble_sdk.Contracts.LoggerContract.LoggerPresenter
import com.desireProj.ble_sdk.MainActivity
import com.desireProj.ble_sdk.ble.BleAdvertiser
import com.desireProj.ble_sdk.ble.BleScanner
import com.desireProj.ble_sdk.diffieHellman.KeyExchanger
import com.desireProj.ble_sdk.model.*

class Engine (loggerPresenter:LoggerContract.LoggerPresenter? = null){
    private lateinit var bleScanner: BleScanner
    private lateinit var bleAdvertiser: BleAdvertiser
    private lateinit var keyExchanger: KeyExchanger
    private lateinit var collectedEbid: CollectedEbid
    private lateinit var loggerDataList: LoggerDataList
    private lateinit var loggerPresenter: LoggerContract.LoggerPresenter
    // TODO to be private
    lateinit var collectedPets: CollectedPets

    init {
        collectedEbid = CollectedEbid(this)
        collectedPets = CollectedPets(this)
        keyExchanger = KeyExchanger(this)
        loggerDataList = LoggerDataList(this)
        bleScanner = BleScanner(this)
        bleAdvertiser = BleAdvertiser()
        this.loggerPresenter = loggerPresenter!!
    }
    fun generateNewKey(){
        keyExchanger.generateNewKeys()
    }
    fun startAdvertising(){
        bleAdvertiser.startAdvertising(keyExchanger.publicKeyByteArray!!)
    }
    fun startScaning(){
        bleScanner.startScanning()
    }
    fun stopScanning(){
        bleScanner.stopScanning()
    }

    fun receiveEbid(dataReceived: ByteArray, rssi: Int) {
        collectedEbid.receiveEbid(dataReceived, rssi)
    }

    fun getPrivateKey(): ByteArray? {
        return (keyExchanger.privateKeyByteArray)
    }

    fun generateSecret(recieved:ByteArray):ByteArray {
        return (keyExchanger.generateSecret(recieved))
    }

    fun generatePet(ebid: EbidReceived) {
        collectedPets.receivedPet(ebid)
    }

    fun addToLogger(petVal: String) {
        Log.e("Engine: addToLogger: ", "petVal = $petVal")
        loggerPresenter.onPetsValueRecieved(petVal)
    }
}