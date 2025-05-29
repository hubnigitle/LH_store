package com.example.lh_store.Models

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class PostProductRequest(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("os")
    val os: String? = null,

    @field:SerializedName("price")
    val price: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("battery")
    val battery: String? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("released")
    val released: Int? = null,

    @field:SerializedName("ram")
    val ram: String? = null
) : Parcelable

