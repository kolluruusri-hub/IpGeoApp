package com.hilton.ipgeoapp.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "http://ip-api.com/"
    private const val TIMEOUT_SECONDS = 15L

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
                            .addInterceptor(logging)
                            .callTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                            .build()

    val api: GeoApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GeoApi::class.java)
}