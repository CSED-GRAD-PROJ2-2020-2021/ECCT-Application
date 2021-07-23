/**
 * Author: Mohamed Samy
 */
package com.ecct.protocol.contracts

import com.ecct.protocol.model.PinCode

interface PinCodeContract {
    interface PinCodeView{
        fun onSuccess()
        fun onFail()
    }
    interface PinCodePresenter{
        fun sendAuthenticationToken(pinCode: PinCode)
    }
}