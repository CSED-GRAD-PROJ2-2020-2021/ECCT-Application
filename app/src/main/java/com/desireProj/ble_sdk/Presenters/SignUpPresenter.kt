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
            object : Callback<AuthenticationToken> {
                override fun onResponse(call: Call<AuthenticationToken>, response: Response<AuthenticationToken>) {
                    val score = response.body()
                    Log.e("phone1", response.body()?.authenticationToken.toString())
                    if(response.code()==200){
                        Log.e("phone2", "sa7 el sa7")
                        val authenticationToken = response.body()?.authenticationToken
                        signUpView.onSuccess(authenticationToken)

                    }
                    else if (response.code()==403){
                        Log.e("phone2", "403 error")
                        signUpView.onFail()
                    }

                    onResult(score)
                }

                override fun onFailure(call: Call<AuthenticationToken>, t: Throwable) {
                    signUpView.onFail()
                    onResult(null)
                }

            }
        )
    }

    override fun restApiSendPhoneNumber(phoneNumber: PhoneNumber) {
        val apiService = RestApiService(context,this)

        apiService.sendPhoneNumber(phoneNumber){
            it?.let { it1 -> sessionManager.saveAuthToken(it1.authenticationToken+"sas") }
        }
    }

}