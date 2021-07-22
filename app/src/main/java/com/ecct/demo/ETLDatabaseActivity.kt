package com.ecct.demo

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecct.protocol.R
import com.ecct.protocol.tools.Engine
import com.ecct.demo.adapters.ETLDatabaseAdapter

class ETLDatabaseActivity : AppCompatActivity() {

    private var etlRecyclerView : RecyclerView? = null
    private var engine : Engine? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)

        engine = Engine

        etlRecyclerView = findViewById(R.id.etl_recycle_view)
        val layoutManger = LinearLayoutManager(this)
        etlRecyclerView!!.layoutManager = layoutManger
        etlRecyclerView!!.adapter = ETLDatabaseAdapter(engine!!.getETLTable())
    }
}