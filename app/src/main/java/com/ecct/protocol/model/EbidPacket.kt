package com.ecct.protocol.model

import java.util.*

/*
    to split ebid into two halves LSB & MSB
    packet1 a byte array of 23 bytes, contains 6 bytes for packet id,
    1 byte for byte index, and 16 byte for ebid half
 */

const val PACKET_SIZE = 23
const val PACKETID_SIZE = 6
const val PACKET_INDEX = 6
const val PACKETSENT_SIZE = 16

class EbidPacket {

    private var ebidByteArray: ByteArray

    var packet1: ByteArray = ByteArray(PACKET_SIZE)

    var packet2: ByteArray = ByteArray(PACKET_SIZE)

    var packetId: ByteArray = ByteArray(PACKETID_SIZE)


    constructor(ebidByteArray : ByteArray) {

        Random().nextBytes(packetId)
        this.ebidByteArray = ebidByteArray
    }

    fun generateEbidPacket(){
        for (i in 0 until PACKETID_SIZE) {
            packet1[i] = packetId[i]
            packet2[i] = packetId[i]
        }
        packet1[PACKET_INDEX] = ebidByteArray[0]    // 0x02 or 0x03 due to bouncy castle DH exchange
        packet2[PACKET_INDEX] = 0x00
        var j = PACKETID_SIZE +1
        for (i in 1..PACKETSENT_SIZE) {
            packet1[j] = ebidByteArray[i]
            packet2[j] = ebidByteArray[i + 16]
            j++
        }
    }

}