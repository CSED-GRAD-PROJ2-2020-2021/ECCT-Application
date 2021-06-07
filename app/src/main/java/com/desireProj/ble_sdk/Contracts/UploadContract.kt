package com.desireProj.ble_sdk.Contracts

import com.desireProj.ble_sdk.model.UploadPetsModel

interface UploadContract {
    interface UploadView{
        fun onSuccess()
    }
    interface UploadPresenter{
        fun uploadPets(uploadPetsModel: UploadPetsModel)
    }
}