/**
 * Author: Mohamed Samy
 */
package com.ecct.protocol.presenters

import android.content.Context
import android.util.Log
import com.ecct.protocol.contracts.PinCodeContract
import com.ecct.protocol.model.AuthenticationTokenResponse
import com.ecct.protocol.model.PinCode
import com.ecct.protocol.network.ApiClient
import com.ecct.protocol.tools.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PinCodePresenter(
    private var pinCodeView: PinCodeContract.PinCodeView,
    private var context: Context
) : PinCodeContract.PinCodePresenter {
    private var apiClient = ApiClient()
    private var sessionManager: SessionManager = SessionManager(context)

    override fun sendAuthenticationToken(pinCode: PinCode) {
        apiClient.getApiService(context).sendAuthenticationToken(pinCode).enqueue(
            object : Callback<AuthenticationTokenResponse> {
                override fun onResponse(call: Call<AuthenticationTokenResponse>, response: Response<AuthenticationTokenResponse>) {
                    //val score = response.body()
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