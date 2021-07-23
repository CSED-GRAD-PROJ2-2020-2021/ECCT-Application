/**
 * Author: Ziad Taha
 */
package com.ecct.protocol.model

import com.google.gson.annotations.SerializedName

class StatusResponse(
    @SerializedName("isAtRisk")
    val status : String
)