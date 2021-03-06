package com.ecct.protocol.pet

import com.ecct.protocol.model.RssiUtility
import com.ecct.protocol.model.Utilities
import java.text.SimpleDateFormat
import java.util.*

const val PET_SIZE = 32
private const val MSECTOMINUTES: Float = 1F/(1000*60)

class Pet {
    var pet: String // H(g^XY) string, used as map key
    var petByteArray: ByteArray // g^XY
    var rssi: RssiUtility
    var duration: Float
    var date: String
    var firstReceived: Long
    var lastReceived: Long
    var greaterSecret: Boolean  // to indicate whether g^A is greater than g^B

    constructor(pet: String, petArr: ByteArray, rssi: RssiUtility, firstRec: Long,
                    lastRec: Long, duration: Float, greaterSecret: Boolean) {
        this.pet = pet
        this.petByteArray = petArr
        this.rssi = rssi
        this.duration = duration
        this.date = getCurrentDate()
        this.firstReceived = firstRec
        this.lastReceived = lastRec
        this.greaterSecret = greaterSecret
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
        this.duration = ((this.lastReceived - this.firstReceived).toFloat() * MSECTOMINUTES)
    }

    private fun getCurrentDate() :String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date: String = sdf.format(Date())

        return (date)
    }
    
}

