package com.desireProj.ble_sdk.database

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import com.desireProj.ble_sdk.model.Utilities
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec

private const val DATABASE_PASSKEY = "PASS KEY"

class PassKeyEncDec {

    companion object {

         var encryptionIv: ByteArray? = null

        @RequiresApi(Build.VERSION_CODES.M)
        fun encryptPassKey(publicPassKey: ByteArray) :ByteArray {
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

            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)

            encryptionIv = cipher.iv

            val encryption: ByteArray = cipher.doFinal(publicPassKey)

            return(encryption)
        }

        fun decrypt(encryptedKey: ByteArray) :String {
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)
            val secretKeyEntry = keyStore
                .getEntry(DATABASE_PASSKEY, null) as KeyStore.SecretKeyEntry

            val secretKey = secretKeyEntry.secretKey

            val cipher = Cipher.getInstance("AES/GCM/NoPadding")

            val spec = GCMParameterSpec(128, encryptionIv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

            val decryption: ByteArray = cipher.doFinal(encryptedKey)

            return(Utilities.byteArrayToString(decryption))
        }
    }

}