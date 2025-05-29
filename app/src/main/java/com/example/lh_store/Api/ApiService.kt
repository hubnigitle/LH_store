package com.example.lh_store.Api

import com.example.lh_store.Models.DeleteProductResponse
import com.example.lh_store.Models.PostProductRequest
import com.example.lh_store.Models.PostProductResponse
import com.example.lh_store.Models.ResponseProduct
import com.example.lh_store.Models.ResponsePromo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("product")
    fun getProducts(): Call<List<ResponseProduct>>

    @GET("promo")
    fun getPromos(): Call<List<ResponsePromo>>

    @POST("product")
    fun postProduct(
        @Body product: PostProductRequest
    ): Call<PostProductResponse>

    @PUT("promo/{id}")
    fun updateProduct(
        @Body product: PostProductRequest,
        @Path("id") id: String
    ): Call<PostProductResponse>

    @DELETE("product/{id}")
    fun deleteProduct(
        @Path("id") id: String
    ): Call<DeleteProductResponse>

}
