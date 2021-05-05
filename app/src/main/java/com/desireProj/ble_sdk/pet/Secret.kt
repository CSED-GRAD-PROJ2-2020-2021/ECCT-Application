package com.desireProj.ble_sdk.pet

import org.bouncycastle.jce.ECNamedCurveTable
import java.security.KeyFactory
import java.security.PublicKey
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec
import org.bouncycastle.jce.spec.ECPublicKeySpec
import java.math.BigInteger
import java.security.PrivateKey
import javax.crypto.KeyAgreement

class Secret(val publicKey: ByteArray?, val privateKey: ByteArray?) {
    fun loadPublicKey(data : ByteArray?):PublicKey {
        val params: ECParameterSpec = ECNamedCurveTable.getParameterSpec("secp256k1")
        val pubKey: ECPublicKeySpec =  ECPublicKeySpec(params.curve.decodePoint(data), params)
        val kf: KeyFactory = KeyFactory.getInstance("ECDH", "BC")
        return kf.generatePublic(pubKey)
    }

    fun loadPrivateKey(data: ByteArray?):PrivateKey {
        val params: ECParameterSpec = ECNamedCurveTable.getParameterSpec("secp256k1")
        val prvKey: ECPrivateKeySpec = ECPrivateKeySpec(BigInteger(data), params)
        val kf: KeyFactory = KeyFactory.getInstance("ECDH", "BC")
        return kf.generatePrivate(prvKey)
    }

    fun doECDH():ByteArray {
        val ka: KeyAgreement = KeyAgreement.getInstance("ECDH", "BC")
        ka.init(loadPrivateKey(privateKey))
        ka.doPhase(loadPublicKey(publicKey),true)
        val secret = ka.generateSecret()
        return secret
    }

}