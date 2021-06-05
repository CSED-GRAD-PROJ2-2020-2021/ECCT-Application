package com.desireProj.ble_sdk.Contracts

import com.desireProj.ble_sdk.model.LoggerData

interface LoggerContract {
    interface LoggerView{
        fun onPetsRecieved(loggerData:LoggerData)
    }
    interface LoggerPresenter{
        fun onPetsValueRecieved(petVal:String)
    }
}