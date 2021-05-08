package com.desireProj.ble_sdk.model

const val idSize = 6
const val packetIndex = 6
const val packet2Index: Byte = 0x00

class CollectedEbid {
    var receivedEbidMap: MutableMap<String, EbidReceived> = mutableMapOf()

    fun receiveEbid(dataReceived: ByteArray, rssi: Int) {
        if (dataReceived.size != 23) return
        val id: String
        val index: Byte = dataReceived[packetIndex]
        val sb:StringBuilder = StringBuilder()
        for (i in 0 until idSize) {
            sb.append(dataReceived[i])
        }
        id = sb.toString()

        if (receivedEbidMap.containsKey(id)) {
            val curEbid: EbidReceived = receivedEbidMap[id]!!
            if (!curEbid.ebidReady) {    // ebid part received before
                updateExistEbid(curEbid, index, dataReceived)
            }
            // update duration
            curEbid.lastReceived = System.currentTimeMillis()
            curEbid.updateDuration()
            // add new rssi
            curEbid.addRssi(rssi)
        } else {
            createNewEbid(id, index, dataReceived, rssi)
        }
    }

    private fun updateExistEbid(curEbid: EbidReceived, index: Byte, data: ByteArray) {
        if (index.equals(packet2Index) && !curEbid.msbReady)
            curEbid.setMsbEbid(data)
        else if (!curEbid.lsbReady)
            curEbid.setLsbEbid(data)
    }

    private fun createNewEbid(id: String, index: Byte, data: ByteArray, rssi: Int) {
        val ebid = EbidReceived()
        ebid.packetId = id
        if (index.equals(packet2Index))
            ebid.setMsbEbid(data)
        else
            ebid.setLsbEbid(data)

        // TODO update first received
        // ebid.setFirstReceived(System.currentTimeMillis())
        ebid.firstReceived = System.currentTimeMillis()
        // update rssi
        ebid.addRssi(rssi)
        receivedEbidMap.put(id, ebid)
    }
}