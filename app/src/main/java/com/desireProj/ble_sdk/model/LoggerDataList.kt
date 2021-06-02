package com.desireProj.ble_sdk.model

import com.desireProj.ble_sdk.tools.Engine

class LoggerDataList(engin:Engine){
    public var loggerDataList : MutableList<LoggerData>? = null
    private var engine: Engine? = null

    init {
        this.engine = engine
        loggerDataList = mutableListOf()
    }
}
