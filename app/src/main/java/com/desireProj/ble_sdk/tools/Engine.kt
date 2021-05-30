package com.desireProj.ble_sdk.tools

import com.desireProj.ble_sdk.ble.BleAdvertiser
import com.desireProj.ble_sdk.ble.BleScanner
import com.desireProj.ble_sdk.diffieHellman.KeyExchanger
import com.desireProj.ble_sdk.model.CollectedEbid
import com.desireProj.ble_sdk.model.CollectedPets

class Engine {
    private lateinit var bleScanner: BleScanner
    private lateinit var bleAdvertiser: BleAdvertiser
    private lateinit var keyExchanger: KeyExchanger
    private lateinit var collectedEbid: CollectedEbid
    private lateinit var collectedPets: CollectedPets

    init {
        collectedEbid = CollectedEbid()
        collectedPets = CollectedPets()
        keyExchanger = KeyExchanger()

        bleScanner = BleScanner(collectedEbid,this)
        bleAdvertiser = BleAdvertiser()
    }
    fun generateNewKey(){
        KeyExchanger.generateNewKeys()
    }
    fun startAdvertising(){
        bleAdvertiser.startAdvertising(KeyExchanger.publicKeyByteArray!!)
    }
    fun startScaning(){
        bleScanner.startScanning()
    }
    fun stop(){
        bleScanner.stopScanning()
    }
    fun generateSecert(recieved:ByteArray){
        var secret = KeyExchanger.generateSecret(recieved)
    }
}