package com.desireProj.ble_sdk.Presenters

import android.util.Log
import com.desireProj.ble_sdk.Contracts.LoggerContract
import com.desireProj.ble_sdk.model.LoggerData

class LoggerPresenter(loggerView:LoggerContract.LoggerView) : LoggerContract.LoggerPresenter {
    private var loggerView : LoggerContract.LoggerView? = null
    private lateinit var loggerData : LoggerData
    init {
        this.loggerView = loggerView
    }
    override fun onPetsValueRecieved(petVal: String) {
        if(petVal == null){
            Log.e("petValNull: ","Null")
        }else {
            loggerData = LoggerData(petVal)
            if(loggerData != null) {
                loggerView!!.onPetsRecieved(loggerData!!)
            }else{
                Log.e("LoggerDataNull: ","Null")
            }
        }
    }
}