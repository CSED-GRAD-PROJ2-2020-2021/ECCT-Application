package com.desireProj.demo

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.desireProj.ble_sdk.R
import com.desireProj.ble_sdk.tools.Engine
import com.desireProj.demo.Adapters.ETLDatabaseAdapter

class ETLDatabaseActivity : AppCompatActivity() {

    private var etlRecyclerView : RecyclerView? = null
    private var engine : Engine? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)

        engine = Engine

        etlRecyclerView = findViewById(R.id.etl_recycle_view)
        var layoutManger = LinearLayoutManager(this)
        etlRecyclerView!!.layoutManager = layoutManger
        etlRecyclerView!!.adapter = ETLDatabaseAdapter(engine!!.getETLTable())
    }
}