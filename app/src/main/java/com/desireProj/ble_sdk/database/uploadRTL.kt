package com.desireProj.ble_sdk.database

import com.google.gson.annotations.SerializedName

data class uploadRTL(
    @SerializedName("PET")
    val pet: String
)