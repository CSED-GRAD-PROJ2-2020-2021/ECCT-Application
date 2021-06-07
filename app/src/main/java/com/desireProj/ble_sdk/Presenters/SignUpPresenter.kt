package com.desireProj.ble_sdk.Presenters

import android.content.Context
import android.util.Log
import com.desireProj.ble_sdk.Contracts.SignUpContract
import com.desireProj.ble_sdk.model.AuthenticationToken
import com.desireProj.ble_sdk.model.PhoneNumber
import com.desireProj.ble_sdk.model.Utilities
import com.desireProj.ble_sdk.network.ApiClient
import com.desireProj.ble_sdk.network.RestApiService
import com.desireProj.ble_sdk.tools.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpPresenter(signUpView: SignUpContract.SignUpView, context:Context) : SignUpContract.SignUpPresenter{
    private var signUpView:SignUpContract.SignUpView
    private var apiClient = ApiClient()
    private lateinit var  context : Context
    private lateinit var sessionManager: SessionManager

    init {
        this.signUpView = signUpView
        this.context = context
        this.sessionManager = SessionManager(context)
    }



    override fun sendPhoneNumber(phoneNumber: PhoneNumber, onResult: (AuthenticationToken?) -> Unit) {
        apiClient.getApiService(context).sendPhoneNumber(phoneNumber).enqueue(
            object : Callback<AuthenticationToken>{
                override fun onResponse(call: Call<AuthenticationToken>, response: Response<AuthenticationToken>) {
                    if(response.headers().get("Authorization")!=null){
                        val headerString : String = response.headers().get("Authorization") as String
                        val authenticationToken = headerString.replace("Bearer ","")
                        sessionManager.saveAuthToken(authenticationToken)
                        if(response.code()==200){
                            Log.e("phone2", "sa7 el sa7")
                            signUpView.onSuccess(authenticationToken)

                        }
                        else if (response.code()==403){
                            Log.e("phone2", "403 error")
                            signUpView.onFail()
                        }
                    }
                    else{
                        Log.e("phone2", "5ara 5ara")
                    }
                    onResult(null)
                }

                override fun onFailure(call: Call<AuthenticationToken>, t: Throwable) {
                    Log.e("phone2", "5ara 5ara")
                    signUpView.onFail()
                    onResult(null)
                }

            }
        )

    }



}