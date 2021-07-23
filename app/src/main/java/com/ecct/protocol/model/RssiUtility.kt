/**
 * Author: Karim Atef
 */
package com.ecct.protocol.model

class RssiUtility {
    // calculating Rssi using moving average method
    private var currentRssi: Int = 0
    var size: Int = 0

    fun addRssi(rssi: Int) {
        this.currentRssi += rssi
        size += 1
    }

    fun getRssi() :Int {
        return this.currentRssi/size
    }
}