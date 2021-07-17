package com.ecct.protocol.model

import com.ecct.protocol.database.UploadETL
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadPetsModel(
    @SerializedName("healthAuthorityToken")
    val authorizationToken:String,
    @Expose
    @SerializedName("infectionPets")
    val pets: List<UploadETL>?
)
