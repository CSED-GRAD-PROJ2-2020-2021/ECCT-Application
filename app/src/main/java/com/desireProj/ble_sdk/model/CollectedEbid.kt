package com.desireProj.ble_sdk.model

const val idSize = 5
const val packetIndex = 6
const val ebidIndex = 7
const val index1: Byte = 0x01
const val index2: Byte = 0x02

class CollectedEbid {
    var receivedEbidMap: HashMap<String, EbidReceived> = hashMapOf()

    fun receiveEbid(dataReceived: ByteArray) {
        if (dataReceived.size != 23) return
        val id: String
        val index: Byte = dataReceived[packetIndex]
        val sb:StringBuilder = StringBuilder()
        for (i in 0..idSize) {
            sb.append(dataReceived[i])
        }
        id = sb.toString()

        if (receivedEbidMap.containsKey(id)) {
            val curEbid: EbidReceived = receivedEbidMap[id]!!
            if (curEbid.ebidReady) {    // ebid already received before
                // TODO update duration, last received
            } else {
                updateExistEbid(curEbid, index, dataReceived)
            }
        } else {
            createNewEbid(id, index, dataReceived)
        }
    }

    private fun updateExistEbid(curEbid: EbidReceived, index: Byte, data: ByteArray) {
        if (index.equals(index1) && !curEbid.msbReady)
            curEbid.setMsbEbid(data)
        else if (index.equals(index2) && !curEbid.lsbReady)
            curEbid.setLsbEbid(data)
    }

    private fun createNewEbid(id: String, index: Byte, data: ByteArray) {
        val ebid = EbidReceived()
        ebid.packetId = id
        if (index.equals(index1))
            ebid.setMsbEbid(data)
        else
            ebid.setLsbEbid(data)
        // TODO update first received
        receivedEbidMap.put(id, ebid)
    }
}