package com.example.lh_store.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lh_store.Api.ApiConfig
import com.example.lh_store.Adapters.ProductAdapter
import com.example.lh_store.Adapters.PromoAdapter
import com.example.lh_store.Models.ResponseProduct
import com.example.lh_store.Models.ResponsePromo
import com.example.lh_store.R
import com.example.lh_store.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter
    private lateinit var promoAdapter: PromoAdapter

    // Search lists
    private var productList = arrayListOf<ResponseProduct>()
    private var searchProductList = arrayListOf<ResponseProduct>()

    private var promoList = arrayListOf<ResponsePromo>()
    private var searchPromoList = arrayListOf<ResponsePromo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvProduct.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPromo.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        fetchProducts()
        fetchPromos()
    }

    override fun onResume() {
        super.onResume()
        // Setup SearchView saat fragment resume (activity sudah siap)
        setupSearchView()
    }

    private fun setupSearchView() {
        val mainActivity = activity

        if (mainActivity != null) {
            val searchView = mainActivity.findViewById<SearchView>(R.id.search)

            if (searchView != null) {
                searchView.clearFocus()
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        searchView.clearFocus()
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        searchProducts(newText)
                        return false
                    }
                })
            } else {
                Toast.makeText(requireContext(), "SearchView tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("HomeFragment", "Activity is null!")
        }
    }

    private fun searchProducts(searchText: String?) {
        searchProductList.clear()
        val searchQuery = searchText?.lowercase(Locale.getDefault()) ?: ""

        if (searchQuery.isNotEmpty()) {
            productList.forEach { product ->
                // Filter berdasarkan field yang ada di ResponseProduct
                val productMatches = listOf(
                    product.name?.lowercase(Locale.getDefault())?.contains(searchQuery) == true,
                    product.category?.lowercase(Locale.getDefault())?.contains(searchQuery) == true,
                    product.os?.lowercase(Locale.getDefault())?.contains(searchQuery) == true
                ).any { it }

                if (productMatches) {
                    searchProductList.add(product)
                }
            }
        } else {
            // Jika search kosong, tampilkan semua produk
            searchProductList.addAll(productList)
        }

        // Update adapter dengan hasil filter
        updateProductAdapter()
    }

    private fun updateProductAdapter() {
        if (::productAdapter.isInitialized) {
            productAdapter = ProductAdapter(searchProductList)
            binding.rvProduct.adapter = productAdapter
        }
    }

    private fun fetchProducts() {
        ApiConfig.getService().getProducts().enqueue(object : Callback<List<ResponseProduct>> {
            override fun onResponse(
                call: Call<List<ResponseProduct>>, response: Response<List<ResponseProduct>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { products ->
                        productList.clear()
                        productList.addAll(products)

                        searchProductList.clear()
                        searchProductList.addAll(products)

                        productAdapter = ProductAdapter(searchProductList)
                        binding.rvProduct.adapter = productAdapter
                    }
                } else {
                    Toast.makeText(requireContext(), "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ResponseProduct>>, t: Throwable) {
                Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchPromos() {
        ApiConfig.getService().getPromos().enqueue(object : Callback<List<ResponsePromo>> {
            override fun onResponse(
                call: Call<List<ResponsePromo>>, response: Response<List<ResponsePromo>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { promos ->
                        promoList.clear()
                        promoList.addAll(promos)

                        searchPromoList.clear()
                        searchPromoList.addAll(promos)

                        promoAdapter = PromoAdapter(searchPromoList)
                        binding.rvPromo.adapter = promoAdapter
                    }
                } else {
                    Toast.makeText(requireContext(), "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ResponsePromo>>, t: Throwable) {
                Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}