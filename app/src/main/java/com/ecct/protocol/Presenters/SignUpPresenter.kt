package com.ecct.protocol.Presenters

import android.content.Context
import android.util.Log
import com.ecct.protocol.Contracts.SignUpContract
import com.ecct.protocol.model.AuthenticationToken
import com.ecct.protocol.model.PhoneNumber
import com.ecct.protocol.network.ApiClient
import com.ecct.protocol.tools.SessionManager
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



    override fun sendPhoneNumber(phoneNumber: PhoneNumber) {
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

                }

                override fun onFailure(call: Call<AuthenticationToken>, t: Throwable) {
                    Log.e("phone2", "5ara 5ara")
                    signUpView.onFail()

                }

            }
        )

    }



}