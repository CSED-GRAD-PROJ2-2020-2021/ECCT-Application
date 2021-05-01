package com.desireProj.ble_sdk.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StoredPETsModel (
    @Expose
    @SerializedName("StoredPets")
    val storedPETs: List<StoredPET>?
){
}