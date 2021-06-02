package com.desireProj.ble_sdk.model

import com.google.gson.annotations.SerializedName

data class AuthenticationToken(

    @SerializedName("authenticationToken")
    var authenticationToken:String,
    @SerializedName("pinCode")
    val pinCode:String,
    @SerializedName("error")
    val error:String
)
