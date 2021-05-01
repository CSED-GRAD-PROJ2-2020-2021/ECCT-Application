package com.desireProj.ble_sdk.network

import com.desireProj.ble_sdk.model.StatusResponse
import com.desireProj.ble_sdk.model.StoredPET
import com.desireProj.ble_sdk.model.StoredPETsModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService {
    fun queryPets(pets: StoredPETsModel, onResult: (StatusResponse?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.queryPets(pets).enqueue(
            object : Callback<StatusResponse>{
                override fun onFailure(call: Call<StatusResponse>, t: Throwable) {
                    onResult(null)
                }

                override fun onResponse(call: Call<StatusResponse>, response: Response<StatusResponse>) {
                    val score = response.body()
                    onResult(score)
                }
            }
        )
    }
    fun uploadPets(pets: StoredPETsModel, onResult: (StatusResponse?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.uploadPets(pets).enqueue(
            object : Callback<StatusResponse>{
                override fun onFailure(call: Call<StatusResponse>, t: Throwable) {
                    onResult(null)
                }

                override fun onResponse(call: Call<StatusResponse>, response: Response<StatusResponse>) {
                    val score = response.body()
                    onResult(score)
                }
            }
        )
    }
}