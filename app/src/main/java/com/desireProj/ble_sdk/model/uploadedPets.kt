package com.desireProj.ble_sdk.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadedPetsModel(
    @SerializedName("key")
    val key:String,
    @SerializedName("id")
    val id:String,
    @Expose
    @SerializedName("pets")
    val pets: List<String>?
)
