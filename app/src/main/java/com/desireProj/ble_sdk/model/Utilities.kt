package com.desireProj.ble_sdk.model

import android.content.Context
import java.lang.StringBuilder
import java.security.MessageDigest
import android.R.id.edit
import android.content.SharedPreferences



class Utilities {

    companion object {
        var context: Context? = null

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

        fun storeBAInSharedPref(str: String, bytes: ByteArray) {
            val settings = context!!.getSharedPreferences("prefs", 0)
            val editor = settings.edit()
            editor.putString(str, byteArrayToString(bytes))
        }

        fun loadBAFromSharedPref(str: String) :ByteArray? {
            val settings = context!!.getSharedPreferences("prefs", 0)
            val stringArray = settings.getString(str, null)

            if (stringArray != null) {
                return (hexStringToByteArray(stringArray)!!)
            }
            return (null)
        }

    }
}