/**
 * Author: Ziad Taha
 */
package com.ecct.protocol.model

import com.google.gson.annotations.SerializedName

data class AuthenticationToken(

    @SerializedName("message")
    var message:String
)
