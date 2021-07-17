package com.ecct.protocol.diffieHellman

import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.interfaces.ECPrivateKey
import org.bouncycastle.jce.interfaces.ECPublicKey
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.jce.spec.ECParameterSpec
import org.bouncycastle.jce.spec.ECPrivateKeySpec
import org.bouncycastle.jce.spec.ECPublicKeySpec
import java.math.BigInteger
import java.security.*
import java.security.spec.ECGenParameterSpec
import javax.crypto.KeyAgreement

class DHTest {

    fun start() {
        Security.addProvider(BouncyCastleProvider())
        val kpgen = KeyPairGenerator.getInstance("ECDH", "BC")
        kpgen.initialize(ECGenParameterSpec("secp256k1"), SecureRandom())
        val pairA = kpgen.generateKeyPair()
        val pairB = kpgen.generateKeyPair()
        println("Alice: " + pairA.private)
        println("Alice: " + pairA.public)
        println("Bob:   " + pairB.private)
        println("Bob:   " + pairB.public)
        val dataPrvA = savePrivateKey(pairA.private)
        val dataPubA = savePublicKey(pairA.public)
        val dataPrvB = savePrivateKey(pairB.private)
        val dataPubB = savePublicKey(pairB.public)
        println("Alice Prv: " + dataPrvA.toHexString())
        println("Alice Prv: " + dataPrvA.toHexString().length / 2)
        println("Alice Pub: " + dataPubA.toHexString())
        println("Alice Pub: " + dataPubA.toHexString().length / 2)
        println("Bob Prv:   " + dataPrvB.toHexString())
        println("Bob Prv:   " + dataPrvB.toHexString().length / 2)
        println("Bob Pub:   " + dataPubB.toHexString())
        println("Bob Pub:   " + dataPubB.toHexString().length / 2)
        doECDH("Alice's secret: ", dataPrvA, dataPubB)
        doECDH("Bob's secret:   ", dataPrvB, dataPubA)
    }

    fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }

    @Throws(Exception::class)
    fun savePublicKey(key: PublicKey): ByteArray { //return key.getEncoded();
        val eckey: ECPublicKey = key as ECPublicKey
        return eckey.getQ().getEncoded(true)
    }

    @Throws(Exception::class)
    fun loadPublicKey(data: ByteArray?): PublicKey { /*KeyFactory kf = KeyFactory.getInstance("ECDH", "BC");
		return kf.generatePublic(new X509EncodedKeySpec(data));*/
        val params: ECParameterSpec = ECNamedCurveTable.getParameterSpec("secp256k1")
        val pubKey = ECPublicKeySpec(
            params.getCurve().decodePoint(data), params
        )
        val kf = KeyFactory.getInstance("ECDH", "BC")
        return kf.generatePublic(pubKey)
    }

    @Throws(Exception::class)
    fun savePrivateKey(key: PrivateKey): ByteArray { //return key.getEncoded();
        val eckey: ECPrivateKey = key as ECPrivateKey
        return eckey.getD().toByteArray()
    }

    @Throws(Exception::class)
    fun loadPrivateKey(data: ByteArray?): PrivateKey {
        val params: ECParameterSpec = ECNamedCurveTable.getParameterSpec("secp256k1")
        val prvkey = ECPrivateKeySpec(BigInteger(data), params)
        val kf = KeyFactory.getInstance("ECDH", "BC")
        return kf.generatePrivate(prvkey)
    }

    @Throws(Exception::class)
    fun doECDH(name: String, dataPrv: ByteArray?, dataPub: ByteArray?) {
        val ka = KeyAgreement.getInstance("ECDH", "BC")
        ka.init(loadPrivateKey(dataPrv))
        ka.doPhase(loadPublicKey(dataPub), true)
        val secret = ka.generateSecret()
        println(name + secret.toHexString())
        println(secret.toHexString().length / 2)
    }

}