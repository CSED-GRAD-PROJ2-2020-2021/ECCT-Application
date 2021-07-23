/**
 * Author: Mohamed Samy
 */
package com.ecct.protocol.model

import com.google.gson.annotations.SerializedName

data class PhoneNumber(
    @SerializedName("phoneNumber")
    val phoneNumber:String

)
