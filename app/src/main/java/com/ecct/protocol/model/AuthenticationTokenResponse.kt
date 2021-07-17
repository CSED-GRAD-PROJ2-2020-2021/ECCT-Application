package com.ecct.protocol.model

import com.google.gson.annotations.SerializedName

data class AuthenticationTokenResponse(
    @SerializedName("id")
    val id:String,
    @SerializedName("key")
    val key:String,
    @SerializedName("iv")
    val iv:String
)
