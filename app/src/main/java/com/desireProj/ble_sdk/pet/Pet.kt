package com.desireProj.ble_sdk.pet

import com.desireProj.ble_sdk.model.RssiUtility
import com.desireProj.ble_sdk.model.Utilities

const val PET_SIZE = 32

class Pet {
    var pet: String // H(g^XY) string, used as map key
    var petByteArray: ByteArray // g^XY
    var rssi: RssiUtility
    var duration: Long
    var firstReceived: Long
    var lastReceived: Long

    constructor(pet: String, petArr: ByteArray, rssi: RssiUtility, firstRec: Long,
                    lastRec: Long, duration: Long) {
        this.pet = pet
        this.petByteArray = petArr
        this.rssi = rssi
        this.duration = duration
        this.firstReceived = firstRec
        this.lastReceived = lastRec
    }

    fun getHash1() :String {
        val byte1: Byte = 0x01
        val newArr: ByteArray = byteArrayOf(byte1, *petByteArray)
        return(Utilities.getHash(newArr))
    }

    fun getHash2() :String {
        val byte2: Byte = 0x02
        val newArr: ByteArray = byteArrayOf(byte2, *petByteArray)
        return(Utilities.getHash(newArr))
    }

    fun addRssi(rssi: Int) {
        this.rssi.addRssi(rssi)
    }

    // return the final value of the rssi
    fun getRssi() :Int {
        return rssi.getRssi()
    }

    fun updateDuration() {
        this.duration = this.lastReceived - this.firstReceived
    }
    
}

