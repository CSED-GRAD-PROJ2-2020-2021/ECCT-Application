package com.desireProj.ble_sdk.Presenters

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.desireProj.ble_sdk.Contracts.PinCodeContract
import com.desireProj.ble_sdk.model.AuthenticationTokenResponse
import com.desireProj.ble_sdk.model.PinCode
import com.desireProj.ble_sdk.network.ApiClient
import com.desireProj.ble_sdk.network.RestApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PinCodePresenter : PinCodeContract.PinCodePresenter {
    private lateinit var pinCodeView: PinCodeContract.PinCodeView
    private lateinit var context: Context
    private var apiClient = ApiClient()
    constructor(pinCodeView:PinCodeContract.PinCodeView, context: Context):super(){
        this.context = context
        this.pinCodeView = pinCodeView
    }
    override fun sendAuthenticationToken(pinCode: PinCode, onResult: (AuthenticationTokenResponse?) -> Unit) {
        apiClient.getApiService(context).sendAuthenticationToken(pinCode).enqueue(
            object : Callback<AuthenticationTokenResponse> {
                override fun onResponse(call: Call<AuthenticationTokenResponse>, response: Response<AuthenticationTokenResponse>) {
                    val score = response.body()
                    Log.e("phone1", response.body()?.id.toString())
                    Log.e("phone2", response.body()?.key.toString())
                    Log.e("phone3", response.body()?.iv.toString())

                    if (response.code() == 200){
                        TODO()
                        //pinCodeView.onSuccess()
                    }else if (response.code() == 403){
                        TODO()
                        //pinCodeView.onFail()
                    }
                    onResult(score)
                }

                override fun onFailure(call: Call<AuthenticationTokenResponse>, t: Throwable) {

                    onResult(null)
                }

            }
        )
    }

    override fun restApiSendAuthenticationToken(pinCode: PinCode) {
        val apiService = RestApiService(context)

        apiService.sendAuthenticationToken(pinCode){

        }
    }
}