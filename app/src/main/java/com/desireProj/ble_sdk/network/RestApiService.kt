package com.desireProj.ble_sdk.network


import android.content.Context
import com.desireProj.ble_sdk.model.StatusResponse
import com.desireProj.ble_sdk.model.StoredPET
import com.desireProj.ble_sdk.model.StoredPETsModel
import com.desireProj.ble_sdk.model.UploadedPetsModel
import android.util.Log
import android.widget.Toast
import com.desireProj.ble_sdk.model.*
import com.desireProj.ble_sdk.tools.SessionManager
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
    fun sendPhoneNumber(phoneNumber:PhoneNumber, onResult: (AuthenticationToken?) -> Unit){
        apiClient.getApiService(context).sendPhoneNumber(phoneNumber).enqueue(
            object : Callback<AuthenticationToken>{
                override fun onResponse(call: Call<AuthenticationToken>, response: Response<AuthenticationToken>) {
                    val score = response.body()
                    Log.e("phone1", response.body()?.authenticationToken.toString())
                    if(response.code()==200){
                        Log.e("phone2", "sa7 el sa7")


                    }
                    else if (response.code()==403){
                        Log.e("phone2", "403 error")

                    }

                    onResult(score)
                }

                override fun onFailure(call: Call<AuthenticationToken>, t: Throwable) {

                    onResult(null)
                }

            }
        )
    }

    fun sendAuthenticationToken(pinCode: PinCode, onResult: (AuthenticationTokenResponse?) -> Unit) {
        apiClient.getApiService(context).sendAuthenticationToken(pinCode).enqueue(
            object : Callback<AuthenticationTokenResponse>{
                override fun onResponse(call: Call<AuthenticationTokenResponse>, response: Response<AuthenticationTokenResponse>) {
                    val score = response.body()
                    Log.e("phone1", response.body()?.id.toString())
                    Log.e("phone2", response.body()?.key.toString())
                    Log.e("phone3", response.body()?.iv.toString())
                    onResult(score)
                }

                override fun onFailure(call: Call<AuthenticationTokenResponse>, t: Throwable) {

                    onResult(null)
                }

            }
        )
    }
}