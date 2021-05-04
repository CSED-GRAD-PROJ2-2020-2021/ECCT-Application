package com.desireProj.ble_sdk.pet

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.Security
import java.security.spec.ECGenParameterSpec

class KeyGenerator {
    val kpgen :KeyPairGenerator = KeyPairGenerator.getInstance("ECDH", "BC")


    init {
        Security.addProvider(BouncyCastleProvider())
        kpgen.initialize(ECGenParameterSpec("prim192v1"), SecureRandom())
    }
    fun generateKeyPair():KeyPair{
        return kpgen.generateKeyPair()
    }

}