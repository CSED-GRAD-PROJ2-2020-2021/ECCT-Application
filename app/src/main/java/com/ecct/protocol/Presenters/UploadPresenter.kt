package com.ecct.protocol.Presenters

import android.content.Context
import com.ecct.protocol.Contracts.UploadContract
import com.ecct.protocol.model.StatusResponse
import com.ecct.protocol.model.UploadPetsModel
import com.ecct.protocol.network.ApiClient
import com.ecct.protocol.tools.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UploadPresenter(uploadView: UploadContract.UploadView,context: Context):UploadContract.UploadPresenter {
    private var uploadView:UploadContract.UploadView
    private var context:Context
    private lateinit var sessionManager:SessionManager
    val apiClient:ApiClient = ApiClient()
    init{
        this.context =context
        this.uploadView = uploadView
        sessionManager = SessionManager(context)
    }
    override fun uploadPets(uploadPetsModel: UploadPetsModel) {
        apiClient.getApiService(context).uploadPets(uploadPetsModel).enqueue(
            object :Callback<StatusResponse>{
                override fun onResponse(
                    call: Call<StatusResponse>,
                    response: Response<StatusResponse>
                ) {
                    val score = response.body()
                    if(response.headers().get("Authorization") != null){
                        val headerString:String = response.headers().get("Authorization") as String
                        val authenticationToken = headerString.replace("Bearer","")
                        sessionManager.saveAuthToken(authenticationToken)
                    }
                    uploadView.onSuccess()
                }

                override fun onFailure(call: Call<StatusResponse>, t: Throwable) {

                }

            }
        )
    }
}