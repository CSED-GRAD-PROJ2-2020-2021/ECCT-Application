package com.desireProj.ble_sdk.database

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import com.desireProj.ble_sdk.model.Utilities
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec


private const val DATABASE_PASSKEY = "PASS KEY"

object PassKeyEncDec {

    fun generatePublicKey() :ByteArray {
        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(128, SecureRandom())
        val originalKey = keygen.generateKey()
        return(originalKey.encoded)
    }

    fun encrypt(key: SecretKey, plainText: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptionIv = cipher.iv
        Utilities.storeBAInSharedPref("CIV", encryptionIv)
        return cipher.doFinal(plainText)
    }

    fun decrypt(secretKey: SecretKey, encrypted: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")

        val encryptionIv = Utilities.loadBAFromSharedPref("CIV")
        val spec = GCMParameterSpec(128, encryptionIv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

        val decryption: ByteArray = cipher.doFinal(encrypted)
        return (decryption)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun generatePrivateKey(): SecretKey {
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

    fun getPrivateKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        val secretKeyEntry = keyStore
            .getEntry(DATABASE_PASSKEY, null) as KeyStore.SecretKeyEntry

        val secretKey: SecretKey = secretKeyEntry.secretKey
        return (secretKey)
    }


//    @RequiresApi(Build.VERSION_CODES.M)
//    fun encryptPassKey(publicPassKey: ByteArray) :ByteArray {
//        val keyGenerator = KeyGenerator
//            .getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
//
//        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
//            DATABASE_PASSKEY,
//            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
//        ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
//            .setKeySize(128)
//            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
//            .build()
//
//        keyGenerator.init(keyGenParameterSpec)
//        val secretKey = keyGenerator.generateKey()
//
//        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
//
//        encryptionIv = cipher.iv
//
//        val encryption: ByteArray = cipher.doFinal(publicPassKey)
//
//        return(encryption)
//    }



//    fun decryptPassKey(encryptedKey: ByteArray) :String {
//        val keyStore = KeyStore.getInstance("AndroidKeyStore")
//        keyStore.load(null)
//        val secretKeyEntry = keyStore
//            .getEntry(DATABASE_PASSKEY, null) as KeyStore.SecretKeyEntry
//
//        val secretKey: SecretKey = secretKeyEntry.secretKey
//
//        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
//
//        val spec = GCMParameterSpec(128, encryptionIv)
//        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
//
//        val decryption: ByteArray = cipher.doFinal(encryptedKey)
//
//        return(Utilities.byteArrayToString(decryption))
//    }

}