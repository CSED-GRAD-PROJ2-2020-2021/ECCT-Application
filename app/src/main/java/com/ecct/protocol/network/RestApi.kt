/**
 * Author: Ziad Taha
 */
package com.ecct.protocol.network

import com.ecct.protocol.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RestApi {

    @Headers("Content-Type: application/json")
    @POST("/exposureRequest")
    fun queryPets(@Body pets: QueryPetsModel): Call<StatusResponse>

    @Headers("Content-Type: application/json")
    @POST("/infectionDeclaration")
    fun uploadPets(@Body pets: UploadPetsModel): Call<StatusResponse>

    @Headers("Content-Type: application/json")
    @POST("/registration")
    fun sendPhoneNumber(@Body phoneNumber: PhoneNumber): Call<AuthenticationToken>
    @Headers("Content-Type: application/json")
    @POST("/regAndAuth")
    fun sendAuthenticationToken(@Body pinCode: PinCode): Call<AuthenticationTokenResponse>
}