/**
 * Author: Karim Atef
 */
package com.ecct.protocol.database

import com.google.gson.annotations.SerializedName

data class UploadETL(
    @SerializedName("PET")
    val pet: String,
    @SerializedName("meetingDate")
    val day: String,
    @SerializedName("duration")
    val duration: Float,
    @SerializedName("RSSI")
    val rssi: Int,
    @SerializedName("uploadDate")
    val uploadDate: String
)