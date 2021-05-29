package com.desireProj.ble_sdk.tools

import com.desireProj.ble_sdk.ble.BleAdvertiser
import com.desireProj.ble_sdk.ble.BleScanner
import com.desireProj.ble_sdk.diffieHellman.KeyExchanger
import com.desireProj.ble_sdk.model.CollectedEbid

class Engine {
    private lateinit var bleScanner: BleScanner
    private lateinit var bleAdvertiser: BleAdvertiser
    private lateinit var keyExchanger: KeyExchanger
    private lateinit var collectedEbid: CollectedEbid
    init {
        collectedEbid = CollectedEbid()
        bleScanner = BleScanner(collectedEbid,this)
        bleAdvertiser = BleAdvertiser()
        keyExchanger = KeyExchanger()
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
    fun stop(){
        bleScanner.stopScanning()
    }
    fun generateSecert(recieved:ByteArray){
        var secret = keyExchanger.generateSecret(recieved)
    }
}