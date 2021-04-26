package com.desireProj.ble_sdk.network

import okhttp3.*

class OkHttpRequest(client: OkHttpClient) {
    internal  var client = OkHttpClient()

    init {
        this.client = client
    }
    fun POST(url: String, parameters: HashMap<String, String>, callback: Callback) :Call {
        val builder = FormBody.Builder()
        val it = parameters.entries.iterator()
        while(it.hasNext()){
            val pair = it.next() as Map.Entry<*, *>
            builder.add(pair.key.toString(), pair.value.toString())
        }

        val formBody = builder.build()
        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()
        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }
    companion object {
        val JSON = MediaType.parse("application/json; charset=utf-8")
    }
}