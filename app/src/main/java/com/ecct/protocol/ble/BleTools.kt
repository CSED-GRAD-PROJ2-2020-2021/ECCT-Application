/**
 * Author: Karim Atef
 */
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

}