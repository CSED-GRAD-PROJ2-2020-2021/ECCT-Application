package com.ecct.protocol.diffieHellman

import com.ecct.protocol.tools.Engine
import java.security.KeyPair

class KeyExchanger(private var engine: Engine) {

    private var convertor:Convertor = Convertor()
    private var privateKeyByteArray: ByteArray? = null
    var publicKeyByteArray: ByteArray? = null
    private var keyGenerator:KeyGenerator = KeyGenerator()
    private lateinit var secret: Secret

    init {
        //generate keys
        val keyPair: KeyPair = keyGenerator.generateKeyPair()
        //private and  public keys
        privateKeyByteArray = convertor.savePrivateKey(keyPair.private)
        publicKeyByteArray = convertor.savePublicKey(keyPair.public)

    }
    fun generateNewKeys(){
        val keyPair: KeyPair = keyGenerator.generateKeyPair()
        //private and  public keys
        privateKeyByteArray = convertor.savePrivateKey(keyPair.private)
        publicKeyByteArray = convertor.savePublicKey(keyPair.public)

    }
    fun generateSecret(received:ByteArray):ByteArray{
        secret = Secret(received, privateKeyByteArray)
        return secret.doECDH()
    }

}