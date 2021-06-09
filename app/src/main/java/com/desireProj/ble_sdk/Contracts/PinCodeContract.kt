package com.desireProj.ble_sdk.Contracts

import com.desireProj.ble_sdk.model.AuthenticationTokenResponse
import com.desireProj.ble_sdk.model.PinCode

interface PinCodeContract {
    interface PinCodeView{
        fun onSuccess()
        fun onFail()
    }
    interface PinCodePresenter{
        fun sendAuthenticationToken(pinCode: PinCode)
        fun restApiSendAuthenticationToken(pinCode: PinCode)
    }
}