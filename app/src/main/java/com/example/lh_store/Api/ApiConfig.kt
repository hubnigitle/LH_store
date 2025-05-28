package com.example.lh_store.Api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private const val BASE_URL = "https://ca2c6010e6a18b2e32b5.free.beeceptor.com/api/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val instance: ApiService by lazy {
        getRetrofit().create(ApiService::class.java)
    }

    fun getService(): ApiService {
        return getRetrofit().create(ApiService::class.java)
    }
}
