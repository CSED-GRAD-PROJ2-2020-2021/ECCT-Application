package com.ecct.protocol.model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.ecct.protocol.database.DataBaseHandler
import com.ecct.protocol.database.ETLItem
import com.ecct.protocol.database.RTLItem
import com.ecct.protocol.pet.Pet
import com.ecct.protocol.tools.Engine

class CollectedPets(engine: Engine) {

    private var engine: Engine

    init {
        this.engine = engine
    }

    private var receivedPetMap: MutableMap<String, Pet> = mutableMapOf()

    fun receivedPet(received: EbidReceived) {
        if (!received.ebidReady) return

        val secret: ByteArray = engine.generateSecret(received.ebid)

        val petVal: String = Utilities.getHash(secret)
        val greaterSecret: Boolean = Utilities.
            byteArrayToString(engine.getPublicKey()!!) > Utilities.
            byteArrayToString(received.ebid)

        Log.e("CollectedPets: ", "received pet: $petVal")

        if (receivedPetMap.containsKey(petVal)) {   // update existing pet
            updateExistingPet(receivedPetMap.get(petVal)!!, received)
        } else {    // create new pet
            val pet = Pet(petVal, secret, received.rssi, received.firstReceived,
                received.lastReceived, received.duration, greaterSecret)

            receivedPetMap.put(petVal, pet)
        }
        this.engine.addToLogger(petVal)
    }

    private fun updateExistingPet(pet: Pet, received: EbidReceived) {
        pet.lastReceived = received.lastReceived
        pet.updateDuration()

        pet.addRssi(received.rssi.getRssi())
    }

    private fun clearMap() {
        this.receivedPetMap.clear()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun sendPetsToDatabase(db: DataBaseHandler) {
        for ((key, pet) in this.receivedPetMap) {
            addToRTL(pet, db)
            addToETL(pet, db)
        }

        clearMap()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun addToRTL(pet: Pet, db: DataBaseHandler) {
        val rtl: RTLItem?
        if (pet.greaterSecret) {
            rtl = RTLItem(pet.getHash1(), pet.date)
        } else {
            rtl = RTLItem(pet.getHash2(), pet.date)
        }
        db.insertRtlItem(rtl)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun addToETL(pet: Pet, db: DataBaseHandler) {
        val etl: ETLItem?
        if (pet.greaterSecret) {    //
            etl = ETLItem(pet.getHash2(), pet.date, pet.duration, pet.getRssi())
        } else {
            etl = ETLItem(pet.getHash1(), pet.date, pet.duration, pet.getRssi())
        }
        db.insertEtlItem(etl)
    }
}