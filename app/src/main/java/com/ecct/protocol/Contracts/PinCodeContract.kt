package com.ecct.protocol.Contracts

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