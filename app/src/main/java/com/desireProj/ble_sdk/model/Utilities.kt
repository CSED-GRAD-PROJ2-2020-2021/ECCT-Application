package com.desireProj.ble_sdk.model

import android.content.Context
import java.lang.StringBuilder
import java.security.MessageDigest

class Utilities {

    companion object {
        // return hash value string for the given byte array
        fun getHash(bytes: ByteArray) :String {
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            return digest.fold("", { str, it -> str + "%02x".format(it) })
        }

        fun byteArrayToString(arr: ByteArray):String {
            val sb = StringBuilder()
            // Iterating through each byte in the array
            for (i in arr) {
                sb.append(String.format("%02X", i))
            }
            return sb.toString()
        }

        fun hexStringToByteArray(str: String): ByteArray? {
            if (str.length % 2 != 0) return null
//            require(length % 2 == 0) {}
            return str.chunked(2)
                .map { it.toInt(16).toByte() }
                .toByteArray()
        }

        var context: Context? = null
    }
}