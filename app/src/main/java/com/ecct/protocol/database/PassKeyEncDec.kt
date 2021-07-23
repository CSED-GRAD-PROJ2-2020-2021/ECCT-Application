/**
 * Author: Karim Atef
 */
package com.ecct.protocol.database

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import com.ecct.protocol.model.Utilities
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec


private const val DATABASE_PASSKEY = "PASS KEY"

object PassKeyEncDec {

    @RequiresApi(Build.VERSION_CODES.M)
    fun initiate() {
        // generate random 128 bit public key (not stored)
        val public: ByteArray = generatePublicKey()
        // generate and store secret key in android keyStore
        generatePrivateKey()
        // load generated secret key from keyStore (cannot be accessed directly)
        val secretKeystore= getPrivateKey()

        // generate and store encrypted password in android shared preferences
        val encrypted = encrypt(secretKeystore, public)
        storeEncryptedPassword(encrypted)
    }

    fun getPasswordString() :String {
        val encrypted = loadEncryptedPassword()
        val decrypted = decrypt(getPrivateKey(), encrypted!!)

        return (Utilities.byteArrayToString(decrypted))
    }

    private fun generatePublicKey() :ByteArray {
        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(128, SecureRandom())
        val originalKey = keygen.generateKey()
        return(originalKey.encoded)
    }

    private fun encrypt(key: SecretKey, plainText: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptionIv = cipher.iv
        storeIV(encryptionIv)
        return cipher.doFinal(plainText)
    }

    private fun decrypt(secretKey: SecretKey, encrypted: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")

        val encryptionIv: ByteArray = loadIV()
        val spec = GCMParameterSpec(128, encryptionIv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

        val decryption: ByteArray = cipher.doFinal(encrypted)
        return (decryption)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun generatePrivateKey(): SecretKey {
        val keyGenerator = KeyGenerator
            .getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")

        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            DATABASE_PASSKEY,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setKeySize(128)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        keyGenerator.init(keyGenParameterSpec)
        val secretKey = keyGenerator.generateKey()
        return (secretKey)
    }

    private fun getPrivateKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        val secretKeyEntry = keyStore
            .getEntry(DATABASE_PASSKEY, null) as KeyStore.SecretKeyEntry

        val secretKey: SecretKey = secretKeyEntry.secretKey
        return (secretKey)
    }

    private fun storeIV(encryptionIv: ByteArray) {
        Utilities.storeBAInSharedPref("CIV", encryptionIv)
    }

    private fun loadIV() :ByteArray {
        return (Utilities.loadBAFromSharedPref("CIV")!!)
    }

    private fun storeEncryptedPassword(encrypted: ByteArray) {
        Utilities.storeBAInSharedPref("Password", encrypted)
    }

    fun loadEncryptedPassword() :ByteArray? {
        return (Utilities.loadBAFromSharedPref("Password"))
    }

}