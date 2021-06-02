package com.desireProj.ble_sdk.model

import com.desireProj.ble_sdk.pet.Pet
import com.desireProj.ble_sdk.tools.Engine

class CollectedPets(engine: Engine) {

    private lateinit var engine: Engine

    init {
        this.engine = engine
    }

    private var receivedPetMap: MutableMap<String, Pet> = mutableMapOf()

    fun receivedPet(received: EbidReceived) {
        if (!received.ebidReady) return

        val secret: ByteArray = engine.generateSecret(received.ebid)

        val petVal: String = Utilities.getHash(secret)
        val greaterSecret: Boolean = Utilities.
            byteArrayToString(engine.getPrivateKey()!!) > Utilities.
            byteArrayToString(received.ebid)

        if (receivedPetMap.containsKey(petVal)) {   // update existing pet
            updateExistingPet(receivedPetMap.get(petVal)!!, received)
        } else {    // create new pet
            var pet: Pet = Pet(petVal, secret, received.rssi, received.firstReceived,
                received.lastReceived, received.duration, greaterSecret)

            receivedPetMap.put(petVal, pet)
        }

    }

    private fun updateExistingPet(pet: Pet, received: EbidReceived) {
        pet.lastReceived = received.lastReceived
        pet.updateDuration()

        pet.addRssi(received.rssi.getRssi())
    }

    private fun clearMap() {
        this.receivedPetMap.clear()
    }

    fun sendPetsToDatabase() {

    }
}