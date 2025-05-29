package com.example.lh_store.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProductViewModel : ViewModel() {
    // Untuk menangani penghapusan produk
    private val _isProductDeleted = MutableLiveData<Boolean>()
    val isProductDeleted: LiveData<Boolean> get() = _isProductDeleted

    // Untuk menangani pembaruan produk
    private val _isProductUpdated = MutableLiveData<Boolean>()
    val isProductUpdated: LiveData<Boolean> get() = _isProductUpdated

    // Fungsi untuk notifikasi penghapusan produk
    fun notifyProductDeleted() {
        _isProductDeleted.value = true
    }

    // Fungsi untuk reset status penghapusan
    fun resetProductDeleted() {
        _isProductDeleted.value = false
    }

    // Fungsi untuk notifikasi pembaruan produk
    fun notifyProductUpdated() {
        _isProductUpdated.value = true
    }

    // Fungsi untuk reset status pembaruan
    fun resetProductUpdated() {
        _isProductUpdated.value = false
    }

    private val _productCount = MutableLiveData<Int>()
    val productCount: LiveData<Int> get() = _productCount

    private val _promoCount = MutableLiveData<Int>()
    val promoCount: LiveData<Int> get() = _promoCount

    fun setProductCount(count: Int) {
        _productCount.value = count
    }
    fun setPromoCount(count: Int) {
        _promoCount.value = count
    }
}