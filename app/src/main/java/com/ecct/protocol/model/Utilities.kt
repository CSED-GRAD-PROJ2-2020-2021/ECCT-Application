/**
 * Author: Karim Atef
 */
package com.ecct.protocol.model

import android.content.Context
import java.lang.StringBuilder
import java.security.MessageDigest
import android.util.Log


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

        fun storeBAInSharedPref(key: String, bytes: ByteArray) {
            val settings = context!!.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val editor = settings.edit()
            editor.putString(key, byteArrayToString(bytes))
            editor.commit()
        }

        fun storeStringInSharedPref(key: String, input: String) {
            val settings = context!!.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val editor = settings.edit()
            editor.putString(key, input)
            editor.commit()
        }

        fun loadStringFromSharedPref(key: String) :String? {
            val settings = context!!.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val result: String? = settings.getString(key, null)
            return (result)
        }

        fun loadBAFromSharedPref(key: String) :ByteArray? {
            Log.d("Utilities: loadBAShPr: ", "utilities context : " + context)
            val settings = context!!.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val stringArray = settings.getString(key, null)

            if (stringArray != null) {
                return (hexStringToByteArray(stringArray)!!)
            }
            return (null)
        }

    }
}