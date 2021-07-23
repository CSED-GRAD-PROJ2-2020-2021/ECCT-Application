/**
 * Author: Mohamed Samy
 */
package com.ecct.demo

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecct.protocol.R
import com.ecct.protocol.tools.Engine
import com.ecct.demo.adapters.RTLDatabaseAdapter

class RTLDatabaseActivity : AppCompatActivity() {

    private var rtlRecyclerView : RecyclerView? = null
    private var engine : Engine? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_r_t_l_database)

        engine = Engine

        rtlRecyclerView = findViewById(R.id.rtl_recycle_view)
        val layoutManger = LinearLayoutManager(this)
        rtlRecyclerView!!.layoutManager = layoutManger
        rtlRecyclerView!!.adapter = RTLDatabaseAdapter(engine!!.getRTLTable())
    }
}