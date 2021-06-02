package com.desireProj.ble_sdk.network

import android.util.Log
import android.widget.Toast
import com.desireProj.ble_sdk.model.*
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
    fun uploadPets(pets: UploadedPetsModel, onResult: (StatusResponse?) -> Unit){
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
    fun sendPhoneNumber(phoneNumber:PhoneNumber, onResult: (AuthenticationToken?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.sendPhoneNumber(phoneNumber).enqueue(
            object : Callback<AuthenticationToken>{
                override fun onResponse(call: Call<AuthenticationToken>, response: Response<AuthenticationToken>) {
                    val score = response.body()
                    Log.e("phone1", response.body()?.authenticationToken.toString())
                    Log.e("phone2", response.body()?.pinCode.toString())
                    Log.e("phone3", response.body()?.error.toString())
                    onResult(score)
                }

                override fun onFailure(call: Call<AuthenticationToken>, t: Throwable) {

                    onResult(null)
                }

            }
        )
    }

    fun sendAuthenticationToken(authenticationToken: AuthenticationToken, onResult: (AuthenticationTokenResponse?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.sendAuthenticationToken(authenticationToken).enqueue(
            object : Callback<AuthenticationTokenResponse>{
                override fun onResponse(call: Call<AuthenticationTokenResponse>, response: Response<AuthenticationTokenResponse>) {
                    val score = response.body()
                    Log.e("phone1", response.body()?.hashedPhoneNumber.toString())
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