package com.desireProj.ble_sdk.Contracts

import com.desireProj.ble_sdk.model.QueryPetsModel
import com.desireProj.ble_sdk.model.StatusResponse
import com.desireProj.ble_sdk.model.StoredPETsModel

interface QueryContract {
    interface QueryView{
       fun onSuccess(statusResponse: StatusResponse)
    }
    interface QueryPresenter{
        fun queryPets(pets: QueryPetsModel)
    }
}