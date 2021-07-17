package com.desireProj.ble_sdk.Presenters

import android.content.Context
import android.util.Log
import com.desireProj.ble_sdk.Contracts.PinCodeContract
import com.desireProj.ble_sdk.model.AuthenticationTokenResponse
import com.desireProj.ble_sdk.model.PinCode
import com.desireProj.ble_sdk.network.ApiClient
import com.desireProj.ble_sdk.tools.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PinCodePresenter : PinCodeContract.PinCodePresenter {
    private lateinit var pinCodeView: PinCodeContract.PinCodeView
    private lateinit var context: Context
    private var apiClient = ApiClient()
    private lateinit var sessionManager: SessionManager
    constructor(pinCodeView:PinCodeContract.PinCodeView, context: Context):super(){
        this.context = context
        this.pinCodeView = pinCodeView
        this.sessionManager = SessionManager(context)
    }
    override fun sendAuthenticationToken(pinCode: PinCode) {
        apiClient.getApiService(context).sendAuthenticationToken(pinCode).enqueue(
            object : Callback<AuthenticationTokenResponse> {
                override fun onResponse(call: Call<AuthenticationTokenResponse>, response: Response<AuthenticationTokenResponse>) {
                    val score = response.body()
                    if(response.headers().get("Authorization") != null){
                        val headerString:String = response.headers().get("Authorization") as String
                        val authenticationToken = headerString.replace("Bearer","")
                        sessionManager.saveAuthToken(authenticationToken)
                    }

                    Log.e("phone1", response.body()?.id.toString())
                    Log.e("phone2", response.body()?.key.toString())
                    Log.e("phone3", response.body()?.iv.toString())

                    sessionManager.saveKeyIdIv(response.body()!!.key, response.body()!!.id,
                        response.body()!!.iv)

                    if (response.code() == 201){

                        pinCodeView.onSuccess()
                    }else if (response.code() == 403){

                        pinCodeView.onFail()
                    }

                }

                override fun onFailure(call: Call<AuthenticationTokenResponse>, t: Throwable) {

                }

            }
        )
    }
}