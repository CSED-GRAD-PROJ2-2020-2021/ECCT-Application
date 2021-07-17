package com.ecct.protocol.database

import com.google.gson.annotations.SerializedName

data class UploadRTL(
    @SerializedName("PET")
    val pet: String
)