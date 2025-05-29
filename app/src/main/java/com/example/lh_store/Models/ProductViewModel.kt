package com.example.lh_store.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProductViewModel : ViewModel() {
    private val _isProductDeleted = MutableLiveData<Boolean>()
    val isProductDeleted: LiveData<Boolean> get() = _isProductDeleted

    fun notifyProductDeleted() {
        _isProductDeleted.value = true
    }

    fun resetProductDeleted() {
        _isProductDeleted.value = false
    }
}
