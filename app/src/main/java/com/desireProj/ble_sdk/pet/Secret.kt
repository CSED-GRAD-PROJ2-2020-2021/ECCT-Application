package com.desireProj.ble_sdk.pet

import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.interfaces.ECPrivateKey
import org.bouncycastle.jce.interfaces.ECPublicKey
import java.security.KeyFactory
import java.security.PublicKey
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec
import org.bouncycastle.jce.spec.ECPublicKeySpec
import java.math.BigInteger
import java.security.PrivateKey
import javax.crypto.KeyAgreement

class Secret(val publicKey: ByteArray, val privateKey: ByteArray) {
    fun loadPublicKey(data : ByteArray):PublicKey {
        val params: ECParameterSpec = ECNamedCurveTable.getParameterSpec("prime192v1")
        val pubKey: ECPublicKeySpec =  ECPublicKeySpec(params.curve.decodePoint(data), params)
        val kf: KeyFactory = KeyFactory.getInstance("ECDH", "BC")
        return kf.generatePublic(pubKey)
    }
    fun savePublicKey(key: PublicKey):ByteArray {
        val ecKey:ECPublicKey = key as ECPublicKey
        return ecKey.q.getEncoded(true)
    }
    fun loadPrivateKey(data: ByteArray):PrivateKey {
        val params: ECParameterSpec = ECNamedCurveTable.getParameterSpec("prime192v1")
        val prvKey: ECPrivateKeySpec = ECPrivateKeySpec(BigInteger(data), params)
        val kf: KeyFactory = KeyFactory.getInstance("ECDH", "BC")
        return kf.generatePrivate(prvKey)
    }
    fun savePrivateKey(key :PublicKey):ByteArray {
        val ecKey:ECPrivateKey = key as ECPrivateKey
        return ecKey.d.toByteArray()
    }
    fun doECDH():ByteArray {
        val ka: KeyAgreement = KeyAgreement.getInstance("ECDH", "BC")
        ka.init(loadPrivateKey(privateKey))
        ka.doPhase(loadPublicKey(publicKey),true)
        val secret = ka.generateSecret()
        return secret
    }

}