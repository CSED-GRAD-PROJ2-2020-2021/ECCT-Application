/**
 * Author: Mohamed Samy
 */
package com.ecct.protocol.contracts

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