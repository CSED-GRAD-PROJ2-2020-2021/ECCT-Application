/**
 * Author: Ziad Taha
 */
package com.ecct.protocol.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class QueryPetsModel (
    @SerializedName("key")
    val key:String,
    @SerializedName("id")
    val id:String,
    @SerializedName("iv")
    val iv:String,
    @Expose
    @SerializedName("exposureTestPets")
    val pets: List<String>?
)