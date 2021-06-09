package com.desireProj.ble_sdk.model

import com.google.gson.annotations.SerializedName

class StatusResponse(
    @SerializedName("isAtRisk")
    val status : String
) {
}