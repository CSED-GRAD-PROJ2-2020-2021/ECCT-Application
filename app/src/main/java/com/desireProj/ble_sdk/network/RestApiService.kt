package com.desireProj.ble_sdk.network


import android.content.Context
import com.desireProj.ble_sdk.model.StatusResponse
import com.desireProj.ble_sdk.model.StoredPET
import com.desireProj.ble_sdk.model.StoredPETsModel
import android.util.Log
import android.widget.Toast
import com.desireProj.ble_sdk.Contracts.PinCodeContract
import com.desireProj.ble_sdk.Contracts.SignUpContract
import com.desireProj.ble_sdk.Presenters.PinCodePresenter
import com.desireProj.ble_sdk.model.*
import com.desireProj.ble_sdk.tools.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService {
    constructor(context: Context):super(){
        this.context = context
        this.sessionManager = SessionManager(context)

    }
    constructor(context: Context, signUpPresenter: SignUpContract.SignUpPresenter):super(){
        this.signUpPresenter = signUpPresenter
        this.context = context
        this.sessionManager = SessionManager(context)
    }

    constructor(context: Context, pinCodePresenter: PinCodeContract.PinCodePresenter):super(){
        this.context = context
        this.pinCodePresenter = pinCodePresenter
        this.sessionManager = SessionManager(context)
    }

    private lateinit var context: Context
    lateinit var apiClient: ApiClient
    private lateinit var signUpPresenter: SignUpContract.SignUpPresenter
    private lateinit var pinCodePresenter:PinCodeContract.PinCodePresenter
    private lateinit var sessionManager: SessionManager
    init {
        apiClient = ApiClient()
    }


    fun sendPhoneNumber(phoneNumber:PhoneNumber, onResult: (AuthenticationToken?) -> Unit){
        signUpPresenter.sendPhoneNumber(phoneNumber)
    }

    fun sendAuthenticationToken(pinCode: PinCode, onResult: (AuthenticationTokenResponse?) -> Unit) {
        pinCodePresenter.sendAuthenticationToken(pinCode)
    }
}
