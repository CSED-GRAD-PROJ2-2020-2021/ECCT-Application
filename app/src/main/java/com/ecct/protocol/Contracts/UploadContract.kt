package com.ecct.protocol.Contracts

import com.ecct.protocol.model.UploadPetsModel

interface UploadContract {
    interface UploadView{
        fun onSuccess()
    }
    interface UploadPresenter{
        fun uploadPets(uploadPetsModel: UploadPetsModel)
    }
}