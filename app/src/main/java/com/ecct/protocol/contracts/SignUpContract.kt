package com.ecct.protocol.contracts

import com.ecct.protocol.model.PhoneNumber

interface SignUpContract {
    interface SignUpView{
        fun onSuccess(authenticationToken: String?)
        fun onFail()
    }
    interface SignUpPresenter{
        fun sendPhoneNumber(phoneNumber: PhoneNumber)

    }
}