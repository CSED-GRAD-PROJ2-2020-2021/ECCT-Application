package com.desireProj.ble_sdk.model

import com.desireProj.ble_sdk.database.RTLItem
import com.desireProj.ble_sdk.database.UploadRTL
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class QueryPetsModel (
    @SerializedName("key")
    val key:String,
    @SerializedName("id")
    val id:String,
    @SerializedName("iv")
    val iv:String,
    @Expose
    @SerializedName("pets")
    val pets: List<UploadRTL>?
)