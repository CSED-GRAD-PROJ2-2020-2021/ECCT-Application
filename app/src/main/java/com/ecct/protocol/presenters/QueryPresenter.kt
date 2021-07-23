/**
 * Author: Mohamed Samy
 */
package com.ecct.protocol.presenters

import android.content.Context
import com.ecct.protocol.contracts.QueryContract
import com.ecct.protocol.model.QueryPetsModel
import com.ecct.protocol.model.StatusResponse
import com.ecct.protocol.network.ApiClient
import com.ecct.protocol.tools.SessionManager
import com.ecct.protocol.tools.log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QueryPresenter(private var queryView: QueryContract.QueryView, private var context: Context):QueryContract.QueryPresenter {
    var sessionManager: SessionManager = SessionManager(context)
    val apiClient:ApiClient = ApiClient()
    override fun queryPets(pets: QueryPetsModel) {
        apiClient.getApiService(context).queryPets(pets).enqueue(
            object : Callback<StatusResponse> {
                override fun onFailure(call: Call<StatusResponse>, t: Throwable) {

                }

                override fun onResponse(call: Call<StatusResponse>, response: Response<StatusResponse>) {
                    val score = response.body()
                    log(score.toString())
                    if(response.headers().get("Authorization") != null){
                        val headerString:String = response.headers().get("Authorization") as String
                        val authenticationToken = headerString.replace("Bearer","")
                        sessionManager.saveAuthToken(authenticationToken)
                    }
                    queryView.onSuccess(score as StatusResponse)

                }
            }
        )
    }
}