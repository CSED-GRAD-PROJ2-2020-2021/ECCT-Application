package com.desireProj.ble_sdk.model

import java.util.*

/*
    to split ebid into two halves LSB & MSB
    packet1 a byte array of 23 bytes, contains 6 bytes for packet id,
    1 byte for byte index, and 16 byte for ebid half
 */

class EbidPacket {
    private var ebid: String
        get() {
            return ebid
        }
        set(value) {
            ebid = value
        }
    var ebidByteArray: ByteArray
        get() {
            return ebidByteArray
        }
        set(value) {
            ebidByteArray = value
        }
    var packet1: ByteArray
        get() {
            return packet1
        }
        set(value) {
            packet1 = value
        }
    var packet2: ByteArray
        get() {
            return packet2
        }
        set(value) {
            packet2 = value
        }
    var packetId: ByteArray
        get() {
            return packetId
        }
        set(value) {
            packetId = value
        }

    constructor(ebid: String) {
        this.ebid = ebid
        this.packet1 = ByteArray(23)
        this.packet2 = ByteArray(23)
        this.packetId = ByteArray(6)
        Random().nextBytes(packetId)
        this.ebidByteArray= ebid.toByteArray()
    }

    fun generateEbidPacket(){
        for (i in 0..5) {
            packet1[i] = packetId[i]
            packet2[i] = packetId[i]
        }
        packet1[6] = 0x01
        packet2[6] = 0x02
        var j = 7
        for (i in 0..15) {
            packet1[j] = ebidByteArray[i]
            packet2[j] = ebidByteArray[i + 16]
            j++
        }
    }

}