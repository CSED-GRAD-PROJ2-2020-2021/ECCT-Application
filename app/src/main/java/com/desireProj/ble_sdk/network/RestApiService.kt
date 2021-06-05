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
        this.sessionManager = SessionManager(context)
    }
    private lateinit var context: Context
    lateinit var apiClient: ApiClient
    lateinit var sessionManager:SessionManager
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
    fun sendPhoneNumber(phoneNumber:PhoneNumber, onResult: (String?) -> Unit){
        apiClient.getApiService(context).sendPhoneNumber(phoneNumber).enqueue(
            object : Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(response.headers().get("Authorization")!=null){
                        val headerString : String = response.headers().get("Authorization") as String
                        sessionManager.saveAuthToken(headerString.replace("Bearer ",""))
                    }
                    onResult(null)
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    onResult(null)
                }

            }
        )
    }

    fun sendAuthenticationToken(pinCode: PinCode, onResult: (AuthenticationTokenResponse?) -> Unit) {
        apiClient.getApiService(context).sendAuthenticationToken(pinCode).enqueue(
            object : Callback<AuthenticationTokenResponse>{
                override fun onResponse(call: Call<AuthenticationTokenResponse>, response: Response<AuthenticationTokenResponse>) {
                    if(response.headers().get("Authorization")!=null){
                        val headerString : String = response.headers().get("Authorization") as String
                        sessionManager.saveAuthToken(headerString.replace("Bearer ",""))
                    }
                    if(response.code()==401){
                        onResult(null)
                    }
                    else if(response.code()==201){
                        val score = response.body()
                        onResult(score)
                    }


                    Log.e("phone1", response.body()?.id.toString())
                    Log.e("phone2", response.body()?.key.toString())
                    Log.e("phone3", response.body()?.iv.toString())

                }

                override fun onFailure(call: Call<AuthenticationTokenResponse>, t: Throwable) {

                    onResult(null)
                }

            }
        )
    }

}