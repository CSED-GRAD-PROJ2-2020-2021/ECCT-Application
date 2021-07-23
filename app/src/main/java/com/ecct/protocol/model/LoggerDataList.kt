/**
 * Author: Mohamed Samy
 */
package com.ecct.protocol.model

import com.ecct.protocol.tools.Engine

class LoggerDataList(engin:Engine){
    public var loggerDataList : MutableList<LoggerData>? = null
    private var engine: Engine? = null

    init {
        this.engine = engine
        loggerDataList = mutableListOf()
    }
}
