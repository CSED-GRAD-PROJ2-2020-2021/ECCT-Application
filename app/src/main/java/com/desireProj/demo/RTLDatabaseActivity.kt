package com.desireProj.demo

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.desireProj.ble_sdk.R
import com.desireProj.ble_sdk.tools.Engine
import com.desireProj.demo.Adapters.RTLDatabaseAdapter

class RTLDatabaseActivity : AppCompatActivity() {

    private var rtlRecyclerView : RecyclerView? = null
    private var engine : Engine? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_r_t_l_database)

        engine = Engine

        rtlRecyclerView = findViewById(R.id.rtl_recycle_view)
        var layoutManger = LinearLayoutManager(this)
        rtlRecyclerView!!.layoutManager = layoutManger
        rtlRecyclerView!!.adapter = RTLDatabaseAdapter(engine!!.getRTLTable())
    }
}