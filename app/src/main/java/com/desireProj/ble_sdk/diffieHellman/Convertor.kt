package com.desireProj.ble_sdk.diffieHellman

import org.bouncycastle.jce.interfaces.ECPrivateKey
import org.bouncycastle.jce.interfaces.ECPublicKey
import java.security.PrivateKey
import java.security.PublicKey

class Convertor {
    fun savePublicKey(key: PublicKey):ByteArray {
        val ecKey: ECPublicKey = key as ECPublicKey
        return ecKey.getQ().getEncoded(true)
    }
    fun savePrivateKey(key :PrivateKey):ByteArray {
        val ecKey: ECPrivateKey = key as ECPrivateKey
        return ecKey.getD().toByteArray()
    }
}