package com.ecct.demo
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.ecct.protocol.model.*
import com.ecct.protocol.tools.*

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecct.protocol.Contracts.LoggerContract
import com.ecct.protocol.Presenters.LoggerPresenter
import com.ecct.protocol.database.DataBaseHandler
import java.lang.StringBuilder
import com.ecct.demo.Adapters.LoggerAdapter
import com.ecct.protocol.R
import kotlin.collections.LinkedHashSet

class MainActivity : AppCompatActivity() , LoggerContract.LoggerView{
    private var mText: TextView? = null
    private var ebitText: TextView? = null
    private var mAdvertiseButton: Button? = null
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var etlDatabaseButton : Button
    private lateinit var rtlDatabaseButton : Button
    private var mDiscoverButton: Button? = null
    private var logger_recycle_view : RecyclerView? = null
    private lateinit var loggerDataListModel :LoggerDataList

    private var engine: Engine? = null
    private var loggerPresenter: LoggerContract.LoggerPresenter? = null

    private lateinit var context:Context

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this

        mText = findViewById(R.id.text_tv)
        ebitText = findViewById(R.id.ebid_tv)
        mDiscoverButton = findViewById(R.id.discover_btn)
        mAdvertiseButton = findViewById(R.id.advertise_btn)
        startButton = findViewById(R.id.start_btn)
        stopButton = findViewById(R.id.stop_btn)
        startButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                actionOnService(Actions.START)
            }})
        stopButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                actionOnService(Actions.STOP)
            }})

        etlDatabaseButton = findViewById(R.id.etl_database_activity_btn)
        etlDatabaseButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(Intent(context,ETLDatabaseActivity::class.java))
            }

        })
        rtlDatabaseButton = findViewById(R.id.rtl_database_activity_btn)
        rtlDatabaseButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(Intent(context,RTLDatabaseActivity::class.java))
            }
        })


        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val locationPermission =
            if (+Build.VERSION.SDK_INT >= 29) Manifest.permission.ACCESS_FINE_LOCATION else Manifest.permission.ACCESS_COARSE_LOCATION
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                Toast.makeText(
                    this,
                    "The permission to get BLE location data is required",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(this, "t3ala", Toast.LENGTH_SHORT).show()
                requestPermissions(arrayOf(locationPermission), 1)
            }
        } else {
            Toast.makeText(this, "Location permissions already granted", Toast.LENGTH_SHORT).show()
        }


        Utilities.context = this

        val database: DataBaseHandler = DataBaseHandler


        var list = database.getRtlItems()
        Log.e("Main Activity", "rtl table size: " + list.size)
        for (rtl in list) {
            Log.e("Main Activity", "rtl item : " + rtl.pet + " date: " + rtl.day)
        }



        var context:Context
        context = this
        loggerPresenter = LoggerPresenter(context)
        engine = Engine
        engine!!.setLoggerPresenter(loggerPresenter as LoggerPresenter)
        engine!!.generateNewKey()
        logger_recycle_view = findViewById(R.id.logger_recycle_view)
        loggerDataListModel = LoggerDataList(engine!!)
        //loggerDataListModel.loggerDataList = loggerDataListModel.loggerDataList
        var layoutManger = LinearLayoutManager(this)
        layoutManger.reverseLayout = true
        layoutManger.stackFromEnd = true
        logger_recycle_view!!.layoutManager = layoutManger

    }

    fun discover(view: View) {
        engine!!.startScanning()
    }

    fun advertise(view: View) {

        engine!!.startAdvertising()
    }



    fun getEbidString(ebid: ByteArray):String {
        val sb = StringBuilder()

        for (i in ebid) {
            sb.append(String.format("%02X", i))
        }
        return sb.toString()
    }
    fun query(view: View) {
       startActivity(Intent(this, QueryActivty::class.java))
    }
    fun upload(view: View){
        startActivity(Intent(this, UploadActivity::class.java))
    }
    private fun actionOnService(action: Actions) {
        if (getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP) return
        Intent(this, BleForegroundService::class.java).also {
            it.action = action.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                log("Starting the service in >=26 Mode")
                startForegroundService(it)
                return
            }
            log("Starting the service in < 26 Mode")
            startService(it)
        }
    }

    override fun onPetsRecieved(loggerData: LoggerData) {
        loggerDataListModel.loggerDataList!!.add(loggerData)
        val list: MutableList<LoggerData> = LinkedHashSet(loggerDataListModel.loggerDataList).toMutableList()
        mText!!.isVisible = true
        mText!!.text = list.size.toString()
        logger_recycle_view!!.adapter = LoggerAdapter(list)
        logger_recycle_view!!.adapter!!.notifyDataSetChanged()
    }

}