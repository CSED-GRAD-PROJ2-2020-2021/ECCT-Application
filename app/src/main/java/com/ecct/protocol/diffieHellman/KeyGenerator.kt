/**
 * Author: Ziad Taha
 */
package com.ecct.protocol.diffieHellman

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.Security
import java.security.spec.ECGenParameterSpec

class KeyGenerator {
    private var  kpgen :KeyPairGenerator

    private fun setupBouncyCastle() {
        val provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
            ?: // Web3j will set up the provider lazily when it's first used.
            return
        if (provider.javaClass.equals(BouncyCastleProvider::class.java)) {
            // BC with same package name, shouldn't happen in real life.
            return
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        Security.insertProviderAt(BouncyCastleProvider(), 1)
    }
    init {
        setupBouncyCastle()
        Security.addProvider(BouncyCastleProvider())
        kpgen = KeyPairGenerator.getInstance("ECDH", "BC")
        kpgen.initialize(ECGenParameterSpec("secp256k1"), SecureRandom())
    }
    fun generateKeyPair():KeyPair{
        return kpgen.generateKeyPair()
    }

}