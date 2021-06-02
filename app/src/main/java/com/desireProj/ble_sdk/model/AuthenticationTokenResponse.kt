package com.desireProj.ble_sdk.model

import com.google.gson.annotations.SerializedName

data class AuthenticationTokenResponse(
    @SerializedName("hashedPhoneNumber")
    val hashedPhoneNumber:String,
    @SerializedName("key")
    val key:String,
    @SerializedName("iv")
    val iv:String
)
