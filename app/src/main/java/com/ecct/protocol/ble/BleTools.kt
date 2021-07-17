package com.ecct.protocol.ble

import android.os.ParcelUuid
import java.util.*

const val idSize = 5
const val packetIndex = 6
const val ebidIndex = 7

class BleTools {

    companion object {
        val pUuid = ParcelUuid(UUID.fromString("0000ccf2-0000-1000-8000-00805F9B34FB"))
    }

    fun extractPetBytes(receivedData: ByteArray) {
        val id: String
        val index: Byte
        val value: String
        if (receivedData.size != 23) return
        val sb:StringBuilder = StringBuilder()
        for (i in 0..idSize) {
            sb.append(receivedData[i])
        }
        id = sb.toString()
        index = receivedData[packetIndex]

        sb.setLength(0)
        for (i in ebidIndex..receivedData.size) {
            sb.append(receivedData[i])
        }
        value = sb.toString()
    }
}