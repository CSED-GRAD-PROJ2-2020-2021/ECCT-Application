package com.desireProj.ble_sdk.diffieHellman

import java.security.KeyPair

class KeyExchanger {
    private lateinit var convertor:Convertor
    var privateKeyByteArray: ByteArray? = null
        get() = field
    var publicKeyByteArray: ByteArray? = null
        get() = field
    private lateinit var keyGenerator:KeyGenerator
    private lateinit var secret: Secret
    init {
        convertor= Convertor()
        keyGenerator = KeyGenerator()
        //genarate keys
        var keyPair: KeyPair = keyGenerator.generateKeyPair()
        //private and  public keys
        privateKeyByteArray = convertor.savePrivateKey(keyPair.private)
        publicKeyByteArray = convertor.savePublicKey(keyPair.public)

    }
    fun generateNewKeys(){
        var keyPair: KeyPair = keyGenerator.generateKeyPair()
        //private and  public keys
        privateKeyByteArray = convertor.savePrivateKey(keyPair.private)
        publicKeyByteArray = convertor.savePublicKey(keyPair.public)

    }
    fun generateSecret(recived:ByteArray):ByteArray{
        secret = Secret(recived, privateKeyByteArray)
        return secret.doECDH()
    }

}