package com.desireProj.ble_sdk.model

import java.security.MessageDigest

class CollectedPets {

    // receivedEbidMap as static map
    companion object {
        var receivedPetMap: MutableMap<String, EbidReceived> = mutableMapOf()
    }

    fun receivedPet() {

    }

    fun getHash(bytes: ByteArray) :String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }
}