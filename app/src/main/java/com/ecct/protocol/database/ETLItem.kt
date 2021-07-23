/**
 * Author: Karim Atef
 */
package com.ecct.protocol.database

class ETLItem {
    val pet: String
    val day: String
    val duration: Float
    val rssi: Int

    constructor(pet: String, day: String, duration: Float, rssi: Int) {
        this.pet = pet
        this.day = day
        this.duration = duration
        this.rssi = rssi
    }
}