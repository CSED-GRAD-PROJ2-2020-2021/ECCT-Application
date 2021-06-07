package com.desireProj.ble_sdk.model

import com.desireProj.ble_sdk.database.ETLItem
import com.desireProj.ble_sdk.database.UploadedETL
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadPetsModel(
    @SerializedName("key")
    val key:String,
    @SerializedName("id")
    val id:String,
    @SerializedName("iv")
    val iv:String,
    @SerializedName("Authorization token")
    val authorizationToken:String,
    @Expose
    @SerializedName("pets")
    val pets: List<UploadedETL>?
)
