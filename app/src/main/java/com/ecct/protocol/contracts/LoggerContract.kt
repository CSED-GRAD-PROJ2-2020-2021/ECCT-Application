package com.ecct.protocol.contracts

import com.ecct.protocol.model.LoggerData

interface LoggerContract {
    interface LoggerView{
        fun onPetsReceived(loggerData:LoggerData)
    }
    interface LoggerPresenter{
        fun onPetsValueReceived(petVal:String)
    }
}