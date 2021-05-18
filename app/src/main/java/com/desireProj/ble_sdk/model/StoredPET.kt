package com.desireProj.ble_sdk.model

import com.google.gson.annotations.SerializedName

data class StoredPET(
    @SerializedName("PET")
    val PETID:String,
    @SerializedName("rssi")
    val RSSI: Int,
    @SerializedName("duration")
    val duration: Double,
    @SerializedName("meetingDate")
    val meetingDate:Long
)
