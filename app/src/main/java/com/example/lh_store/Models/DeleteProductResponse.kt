package com.example.lh_store.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeleteProductResponse(
    val success: Boolean,
    val message: String
): Parcelable