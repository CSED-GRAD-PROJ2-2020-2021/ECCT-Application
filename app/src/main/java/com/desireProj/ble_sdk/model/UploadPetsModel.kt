package com.desireProj.ble_sdk.model

import com.desireProj.ble_sdk.database.UploadETL
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadPetsModel(
    @SerializedName("healthAuthorityToken")
    val authorizationToken:String,
    @Expose
    @SerializedName("infectionPets")
    val pets: List<UploadETL>?
)
