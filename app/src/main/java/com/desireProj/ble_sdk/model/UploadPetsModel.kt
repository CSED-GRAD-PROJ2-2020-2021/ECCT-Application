package com.desireProj.ble_sdk.model

import com.desireProj.ble_sdk.database.ETLItem
import com.desireProj.ble_sdk.database.UploadETL
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadPetsModel(
    @SerializedName("Authorization token")
    val authorizationToken:String,
    @Expose
    @SerializedName("pets")
    val pets: List<UploadETL>?
)
