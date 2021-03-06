package com.ecct.protocol.diffieHellman

import org.bouncycastle.jce.ECNamedCurveTable
import java.security.KeyFactory
import java.security.PublicKey
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec
import org.bouncycastle.jce.spec.ECPublicKeySpec
import java.math.BigInteger
import java.security.PrivateKey
import javax.crypto.KeyAgreement

class Secret {
    var publicKey: ByteArray? = null
    var privateKey: ByteArray? = null

    constructor(publicKey: ByteArray?, privateKey: ByteArray?) {
        this.publicKey = publicKey
        this.privateKey = privateKey
    }

    private fun loadPublicKey(data : ByteArray?):PublicKey {
        val params: ECParameterSpec = ECNamedCurveTable.getParameterSpec("secp256k1")
        val pubKey: ECPublicKeySpec =  ECPublicKeySpec(params.curve.decodePoint(data), params)
        val kf: KeyFactory = KeyFactory.getInstance("ECDH", "BC")
        return kf.generatePublic(pubKey)
    }

    private fun loadPrivateKey(data: ByteArray?):PrivateKey {
        val params: ECParameterSpec = ECNamedCurveTable.getParameterSpec("secp256k1")
        val prvKey: ECPrivateKeySpec = ECPrivateKeySpec(BigInteger(data), params)
        val kf: KeyFactory = KeyFactory.getInstance("ECDH", "BC")
        return kf.generatePrivate(prvKey)
    }

    fun doECDH() :ByteArray {
        val ka = KeyAgreement.getInstance("ECDH", "BC")
        ka.init(loadPrivateKey(this.privateKey))
        ka.doPhase(loadPublicKey(this.publicKey), true)
        val secret: ByteArray = ka.generateSecret()
        return(secret)
    }

}