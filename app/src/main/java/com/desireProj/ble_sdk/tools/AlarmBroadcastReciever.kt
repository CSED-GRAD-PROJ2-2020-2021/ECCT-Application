package com.desireProj.ble_sdk.tools

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import java.security.KeyPair
import com.desireProj.ble_sdk.diffieHellman.KeyGenerator

class AlarmBroadcastReciever:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val keyGenerator: KeyGenerator = KeyGenerator()
        var keyPair: KeyPair = keyGenerator.generateKeyPair()
        log(keyPair.public.encoded.toString())
        Toast.makeText(context,keyPair.public.encoded.toString(),Toast.LENGTH_SHORT).show();
    }
}