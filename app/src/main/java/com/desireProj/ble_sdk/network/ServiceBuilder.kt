package com.desireProj.ble_sdk.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    val interceptor = HttpLoggingInterceptor()
    init {
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        println("https://10.0.2.201:3000/registration")

    }


    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build();
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.201:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}