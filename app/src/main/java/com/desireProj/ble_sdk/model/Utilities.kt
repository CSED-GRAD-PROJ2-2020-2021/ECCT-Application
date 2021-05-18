package com.desireProj.ble_sdk.model

import java.security.MessageDigest

class Utilities {

    companion object {
        // return hash value string for the given byte array
        fun getHash(bytes: ByteArray) :String {
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            return digest.fold("", { str, it -> str + "%02x".format(it) })
        }
    }
}