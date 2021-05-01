package com.desireProj.ble_sdk.network

import com.desireProj.ble_sdk.model.StatusResponse
import com.desireProj.ble_sdk.model.StoredPETsModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RestApi {

    @Headers("Content-Type: application/json")
    @POST("pets")
    fun queryPets(@Body pets: StoredPETsModel): Call<StatusResponse>

    @Headers("Content-Type: application/json")
    @POST("pets")
    fun uploadPets(@Body pets: StoredPETsModel): Call<StatusResponse>

}