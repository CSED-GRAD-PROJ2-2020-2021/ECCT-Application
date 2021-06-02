package com.desireProj.ble_sdk.network

import android.content.Context
import com.desireProj.ble_sdk.model.StatusResponse
import com.desireProj.ble_sdk.model.StoredPET
import com.desireProj.ble_sdk.model.StoredPETsModel
import com.desireProj.ble_sdk.model.UploadedPetsModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService {
    constructor(context: Context){
        this.context = context
    }
    private lateinit var context: Context
    lateinit var apiClient: ApiClient
    init {
        apiClient = ApiClient()
    }
    fun queryPets(pets: StoredPETsModel, onResult: (StatusResponse?) -> Unit){
        apiClient.getApiService(context).queryPets(pets).enqueue(
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
    fun uploadPets(pets: UploadedPetsModel, onResult: (StatusResponse?) -> Unit ){
        apiClient.getApiService(context).uploadPets(pets).enqueue(
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