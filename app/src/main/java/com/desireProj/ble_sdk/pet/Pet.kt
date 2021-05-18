package com.desireProj.ble_sdk.pet

import java.security.MessageDigest

const val PET_SIZE = 32

class Pet {
    var petByteArray: ByteArray
    var rssiList: MutableList<Int>
    var duration: Long
    var firstReceived: Long
    var lastReceived: Long

    constructor(petArr: ByteArray, rssiList: MutableList<Int>, firstRec: Long, lastRec: Long, duration: Long) {
        this.petByteArray = petArr
        this.rssiList = rssiList
        this.duration = duration
        this.firstReceived = firstRec
        this.lastReceived = lastRec
    }

    fun getHash1() :String {
        val byte1: Byte = 0x01
        val newArr: ByteArray = byteArrayOf(byte1, *petByteArray)
        return(getHash(newArr))
    }

    fun getHash2() :String {
        val byte2: Byte = 0x02
        val newArr: ByteArray = byteArrayOf(byte2, *petByteArray)
        return(getHash(newArr))
    }

    fun addRssi(rssi: Int) {
        rssiList.add(rssi)
    }

    fun calculateRssi() :Int {
        return rssiList.average().toInt()
    }

    fun updateDuration() {
        this.lastReceived = System.currentTimeMillis()
        this.duration = this.lastReceived - this.firstReceived
    }

    fun getHash(bytes: ByteArray) :String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }
    
}

