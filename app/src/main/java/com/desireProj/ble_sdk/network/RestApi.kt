package com.desireProj.ble_sdk.network

import com.desireProj.ble_sdk.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface RestApi {

    @Headers("Content-Type: application/json")
    @POST("/registration")
    fun queryPets(@Body pets: StoredPETsModel): Call<StatusResponse>

    @Headers("Content-Type: application/json")
    @POST("/test")
    fun uploadPets(@Body pets: UploadedPetsModel): Call<StatusResponse>

    @Headers("Content-Type: application/json")
    @POST("/registration")
    fun sendPhoneNumber(@Body phoneNumber: PhoneNumber): Call<String>
    @Headers("Content-Type: application/json")
    @POST("/regAndAuth")
    fun sendAuthenticationToken(@Body pinCode: PinCode): Call<AuthenticationTokenResponse>
}