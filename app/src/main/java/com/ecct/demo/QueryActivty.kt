package com.ecct.demo

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ecct.protocol.contracts.QueryContract
import com.ecct.protocol.presenters.QueryPresenter
import com.ecct.protocol.R
import com.ecct.protocol.model.*
import com.ecct.protocol.tools.Engine
import com.ecct.protocol.tools.SessionManager
import com.ecct.protocol.tools.log

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
        with(queryResultButton) {
            setOnClickListener {
                val queryPets = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    QueryPetsModel(
                        sessionManager.fetchKey()!!,
                        sessionManager.fetchID()!!, sessionManager.fetchIv()!!, engine.getRTLList()
                    )
                } else {
                    TODO("VERSION.SDK_INT < M")
                }
                log(queryPets.pets.toString())
                queryPresenter.queryPets(queryPets)
            }
        }
    }

    override fun onSuccess(statusResponse: StatusResponse) {
        queryResultText?.setText(statusResponse.status)
    }

}