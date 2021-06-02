package com.desireProj.ble_sdk.network

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    private lateinit var apiService: RestApi
    val interceptor = HttpLoggingInterceptor()
    init {
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        //println("https://10.0.2.201:3000/registration")
    }

    fun getApiService(context: Context): RestApi {

        // Initialize ApiService if not initialized yet
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.3.106:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient(context))
                .build()

            apiService = retrofit.create(RestApi::class.java)
        }

        return apiService
    }

    /**
     * Initialize OkhttpClient with our interceptor
     */
    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .addInterceptor(interceptor)
            .build()
    }
}