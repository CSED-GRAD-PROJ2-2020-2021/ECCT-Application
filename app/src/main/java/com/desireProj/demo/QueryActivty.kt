package com.desireProj.demo

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.desireProj.ble_sdk.Contracts.QueryContract
import com.desireProj.ble_sdk.Presenters.QueryPresenter
import com.desireProj.ble_sdk.R
import com.desireProj.ble_sdk.model.*
import com.desireProj.ble_sdk.tools.Engine
import com.desireProj.ble_sdk.tools.SessionManager

class QueryActivty:AppCompatActivity() ,QueryContract.QueryView{
    private lateinit var  queryResultButton : Button
    private var queryResultText : TextView? = null
    private val context : Context = this
    private lateinit var sessionManager: SessionManager
    private lateinit var queryPresenter: QueryPresenter
    private lateinit var engine:Engine
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.query_activity)
        queryResultButton = findViewById(R.id.button_query)
        queryResultText=findViewById(R.id.query_result)
        queryPresenter=QueryPresenter(this,context)
        sessionManager = SessionManager(context)
        engine = Engine
        queryResultButton.setOnClickListener(object : View.OnClickListener{
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onClick(v: View?) {
                val queryPets:QueryPetsModel = QueryPetsModel(sessionManager.fetchKey()!!,
                sessionManager.fetchID()!!,sessionManager.fetchIv()!!,engine.getRTLList())
                queryPresenter.queryPets(queryPets)
            }
        })
    }

    override fun onSuccess(statusResponse: StatusResponse) {
        queryResultText?.setText(statusResponse.status)
    }

}