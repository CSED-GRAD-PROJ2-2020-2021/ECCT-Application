package com.desireProj.ble_sdk.network

import com.desireProj.ble_sdk.model.StatusResponse
import com.desireProj.ble_sdk.model.StoredPETsModel
import com.desireProj.ble_sdk.model.UploadedPetsModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RestApi {

    @Headers("Content-Type: application/json")
    @POST("/registration")
    fun queryPets(@Body pets: StoredPETsModel): Call<StatusResponse>

    @Headers("Content-Type: application/json")
    @POST("/test")
    fun uploadPets(@Body pets: UploadedPetsModel): Call<StatusResponse>

}