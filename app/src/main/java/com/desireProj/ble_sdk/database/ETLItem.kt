package com.desireProj.ble_sdk.database

class ETLItem {
    val pet: String
    val day: String
    val duration: Long
    val rssi: Int

    constructor(pet: String, day: String, duration: Long, rssi: Int) {
        this.pet = pet
        this.day = day
        this.duration = duration
        this.rssi = rssi
    }
}