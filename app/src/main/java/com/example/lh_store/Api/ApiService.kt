package com.example.lh_store.Api

import com.example.lh_store.Models.ResponseProduct
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("product")
    fun getProducts(): Call<List<ResponseProduct>>
}
