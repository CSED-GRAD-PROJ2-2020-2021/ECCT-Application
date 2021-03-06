package com.ecct.protocol.model

const val EBID_SIZE = 33
const val PACKET_Size = 23
const val LSB_INDEX = 0
const val LSB_EBID_INDEX = 6
const val MSB_INDEX = 17
const val MSB_EBID_INDEX = 7
private const val MSECTOMINUTES = 1F/(1000*60)

class EbidReceived {
    var ebid: ByteArray
    var packetId: String
    val rssi: RssiUtility
    var duration: Float
    var firstReceived: Long
    var lastReceived: Long
    var ebidReady: Boolean
    var lsbReady: Boolean
    var msbReady: Boolean

    constructor() {
        ebid = ByteArray(EBID_SIZE)
        packetId = ""
        rssi = RssiUtility()
        duration = 0.0F
        firstReceived = 0
        lastReceived = 0
        ebidReady = false
        lsbReady = false
        msbReady = false
    }

    fun setLsbEbid(lsb: ByteArray): Boolean {
        if (lsb.size != PACKET_Size) return false

        var j = LSB_INDEX
        for (i in LSB_EBID_INDEX until PACKET_Size) {    // 17 bytes
            this.ebid[j++] = lsb[i]
        }
        lsbReady = true
        this.ebidReady = this.msbReady

        return true
    }

    fun setMsbEbid(msb: ByteArray): Boolean {
        if (msb.size != PACKET_Size) return false

        var j = MSB_INDEX
        for (i in MSB_EBID_INDEX until PACKET_Size) {
            this.ebid[j++] = msb[i]
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

    fun addRssi(rssi: Int) {
        this.rssi.addRssi(rssi)
    }

    fun getRssi() :Int {
        return(rssi.getRssi())
    }

    fun updateDuration() {
        this.duration = ((this.lastReceived - this.firstReceived).toFloat() * MSECTOMINUTES)
    }

    // TODO update duration
//    fun setFirstReceived(duration: Long) {
//        this.firstReceived = duration
//    }
}