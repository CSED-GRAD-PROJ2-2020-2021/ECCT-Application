package com.desireProj.ble_sdk.model

class EbidReceived {
    var ebid: ByteArray
    var packetId: String
    var rssiList: List<Int>
    var duration: Int
    var firstReceived: Int
    var lastReceived: Int
    var ebidReady: Boolean
    var lsbReady: Boolean
    var msbReady: Boolean

    constructor() {
        ebid = ByteArray(32)
        packetId = ""
        rssiList = emptyList()
        duration = 0
        firstReceived = 0
        lastReceived = 0
        ebidReady = false
        lsbReady = false
        msbReady = false
    }

    fun setLsbEbid(lsb: ByteArray): Boolean {
        if (lsb.size != 23) return false

        var j = 16
        for (i in 7..lsb.size) {
            this.ebid[j++] = lsb[i]
        }
        lsbReady = true
        this.ebidReady = this.msbReady
        return true
    }

    fun setMsbEbid(lsb: ByteArray): Boolean {
        if (lsb.size != 23) return false

        var j = 0
        for (i in 7..lsb.size) {
            this.ebid[j++] = lsb[i]
        }
        msbReady = true
        this.ebidReady = this.lsbReady
        return true
    }

    fun getEbidString():String {
        val sb = StringBuilder()
        // Iterating through each byte in the array
        for (i in ebid) {
            sb.append(String.format("%02X", i))
        }
        return sb.toString()
    }

    // TODO check
    fun addRssi(rssi: Int) {
        this.rssiList += rssi
    }

    // TODO update duration

}