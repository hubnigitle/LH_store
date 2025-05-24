package com.example.lh_store.Api

import com.example.lh_store.Models.ResponseProduct
import com.example.lh_store.Models.ResponsePromo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("product")
    fun getProducts(): Call<List<ResponseProduct>>

    @GET("promo")
    fun getPromos(): Call<List<ResponsePromo>>
}
