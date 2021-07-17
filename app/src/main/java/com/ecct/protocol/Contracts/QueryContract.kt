package com.ecct.protocol.Contracts

import com.ecct.protocol.model.QueryPetsModel
import com.ecct.protocol.model.StatusResponse

interface QueryContract {
    interface QueryView{
       fun onSuccess(statusResponse: StatusResponse)
    }
    interface QueryPresenter{
        fun queryPets(pets: QueryPetsModel)
    }
}