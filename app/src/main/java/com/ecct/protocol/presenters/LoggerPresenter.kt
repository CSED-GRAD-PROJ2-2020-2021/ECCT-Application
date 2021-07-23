/**
 * Author: Mohamed Samy
 */
package com.ecct.protocol.presenters

import com.ecct.protocol.contracts.LoggerContract
import com.ecct.protocol.model.LoggerData

class LoggerPresenter(loggerView:LoggerContract.LoggerView) : LoggerContract.LoggerPresenter {
    private var loggerView : LoggerContract.LoggerView? = null
    private lateinit var loggerData : LoggerData
    init {
        this.loggerView = loggerView
    }
    override fun onPetsValueReceived(petVal: String) {
        loggerData = LoggerData(petVal)
        loggerView!!.onPetsReceived(loggerData)
    }
}