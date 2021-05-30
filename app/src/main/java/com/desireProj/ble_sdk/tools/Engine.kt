package com.desireProj.ble_sdk.tools

import com.desireProj.ble_sdk.ble.BleAdvertiser
import com.desireProj.ble_sdk.ble.BleScanner
import com.desireProj.ble_sdk.diffieHellman.KeyExchanger
import com.desireProj.ble_sdk.model.CollectedEbid
import com.desireProj.ble_sdk.model.CollectedPets
import com.desireProj.ble_sdk.model.EbidReceived

class Engine {
    private lateinit var bleScanner: BleScanner
    private lateinit var bleAdvertiser: BleAdvertiser
    private lateinit var keyExchanger: KeyExchanger
    private lateinit var collectedEbid: CollectedEbid
    private lateinit var collectedPets: CollectedPets

    init {
        collectedEbid = CollectedEbid(this)
        collectedPets = CollectedPets(this)
        keyExchanger = KeyExchanger(this)

        bleScanner = BleScanner(this)
        bleAdvertiser = BleAdvertiser()
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
}