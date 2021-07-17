package com.ecct.protocol.Contracts

import com.ecct.protocol.model.LoggerData

interface LoggerContract {
    interface LoggerView{
        fun onPetsRecieved(loggerData:LoggerData)
    }
    interface LoggerPresenter{
        fun onPetsValueRecieved(petVal:String)
    }
}