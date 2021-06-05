package com.desireProj.ble_sdk.tools

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.desireProj.ble_sdk.Contracts.LoggerContract
import com.desireProj.ble_sdk.ble.BleAdvertiser
import com.desireProj.ble_sdk.ble.BleScanner
import com.desireProj.ble_sdk.database.DataBaseHandler
import com.desireProj.ble_sdk.diffieHellman.KeyExchanger
import com.desireProj.ble_sdk.model.*

class Engine {
    constructor(loggerPresenter:LoggerContract.LoggerPresenter? = null) : super() {
        this.loggerPresenter = loggerPresenter!!
    }
    constructor():super(){}
    private lateinit var bleScanner: BleScanner
    private lateinit var bleAdvertiser: BleAdvertiser
    private lateinit var keyExchanger: KeyExchanger
    private lateinit var collectedEbid: CollectedEbid
    private lateinit var loggerDataList: LoggerDataList
    private lateinit var loggerPresenter: LoggerContract.LoggerPresenter
    // TODO to be private
    lateinit var collectedPets: CollectedPets
    private lateinit var dataBaseHandler: DataBaseHandler

    
    init {
        collectedEbid = CollectedEbid(this)
        collectedPets = CollectedPets(this)
        keyExchanger = KeyExchanger(this)
        dataBaseHandler = DataBaseHandler

        loggerDataList = LoggerDataList(this)

        bleScanner = BleScanner(this)
        bleAdvertiser = BleAdvertiser()
    }
    fun generateNewKey(){
        keyExchanger.generateNewKeys()
    }
    fun startAdvertising(){
        bleAdvertiser.startAdvertising(keyExchanger.publicKeyByteArray!!)
    }
    fun startScanning(){
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

    fun generateSecret(received: ByteArray):ByteArray {
        return (keyExchanger.generateSecret(received))
    }

    fun generatePet(ebid: EbidReceived) {
        collectedPets.receivedPet(ebid)
    }

    fun addToLogger(petVal: String) {
        Log.e("Engine: addToLogger: ", "petVal = $petVal")
        loggerPresenter.onPetsValueRecieved(petVal)
    }

    fun clearEbidMap() {
        this.collectedEbid.clearMap()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun sendPetsToDatabase() {
        this.collectedPets.sendPetsToDatabase(dataBaseHandler)
    }
}
