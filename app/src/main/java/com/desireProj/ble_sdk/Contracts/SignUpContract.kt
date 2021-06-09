package com.desireProj.ble_sdk.Contracts

import com.desireProj.ble_sdk.model.AuthenticationToken
import com.desireProj.ble_sdk.model.PhoneNumber

interface SignUpContract {
    interface SignUpView{
        fun onSuccess(authenticationToken: String?)
        fun onFail()
    }
    interface SignUpPresenter{
        fun sendPhoneNumber(phoneNumber: PhoneNumber)

    }
}