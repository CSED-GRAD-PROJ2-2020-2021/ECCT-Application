package com.desireProj.ble_sdk.database

class ETLItem {
    val pet:String
    val day:String // TODO to be checked later
    val time:Long
    val rssi:Int

    constructor(pet:String, day:String, time:Long, rssi:Int) {
        this.pet = pet
        this.day = day
        this.time = time
        this.rssi = rssi
    }
}