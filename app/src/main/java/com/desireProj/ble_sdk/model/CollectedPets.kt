package com.desireProj.ble_sdk.model

import com.desireProj.ble_sdk.pet.Pet

class CollectedPets {

    companion object {
        var receivedPetMap: MutableMap<String, Pet> = mutableMapOf()

        fun receivedPet(received: EbidReceived) {
            if (!received.ebidReady) return


            val petVal: String = Utilities.getHash(received.ebid)

            if (receivedPetMap.containsKey(petVal)) {   // update existing pet
                updateExistingPet(receivedPetMap.get(petVal)!!, received)
            } else {    // create new pet
                var pet: Pet = Pet(petVal, received.ebid, received.rssi, received.firstReceived,
                    received.lastReceived, received.duration)

                receivedPetMap.put(petVal, pet)
            }

        }

        private fun updateExistingPet(pet: Pet, received: EbidReceived) {
            pet.lastReceived = received.lastReceived
            pet.updateDuration()

            pet.rssi.addRssi(received.rssi.getRssi())
        }
    }





}