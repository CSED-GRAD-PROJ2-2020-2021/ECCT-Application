package com.desireProj.ble_sdk.database

import com.google.gson.annotations.SerializedName

data class UploadETL(
    @SerializedName("PET")
    val pet: String,
    @SerializedName("meetingDate")
    val day: String,
    @SerializedName("duration")
    val duration: Long,
    @SerializedName("RSSI")
    val rssi: Int,
    @SerializedName("uploadDate")
    val uploadDate: String
)