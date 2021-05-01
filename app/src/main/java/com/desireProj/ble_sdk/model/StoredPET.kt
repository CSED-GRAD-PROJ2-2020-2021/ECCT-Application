package com.desireProj.ble_sdk.model

import com.google.gson.annotations.SerializedName
import java.time.Duration
import java.util.*

data class StoredPET (
    @SerializedName("PET")
    val PETID :String,
    @SerializedName("distance")
    val distance:Double,
    @SerializedName("duration")
    val duration: Duration,
    @SerializedName("date")
    val date : Date
)