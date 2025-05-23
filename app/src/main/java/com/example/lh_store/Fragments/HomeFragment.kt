package com.example.lh_store.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lh_store.Api.ApiConfig
import com.example.lh_store.Adapters.ProductAdapter
import com.example.lh_store.Adapters.PromoAdapter
import com.example.lh_store.Models.ResponseProduct
import com.example.lh_store.Models.ResponsePromo
import com.example.lh_store.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter
    private lateinit var promoAdapter: PromoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvProduct.layoutManager = LinearLayoutManager(requireContext())
        fetchProducts()
        binding.rvPromo.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        fetchPromos()
    }

    private fun fetchProducts() {
        ApiConfig.getService().getProducts().enqueue(object : Callback<List<ResponseProduct>> {
            override fun onResponse(
                call: Call<List<ResponseProduct>>, response: Response<List<ResponseProduct>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { products ->
                        productAdapter = ProductAdapter(products)
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
                        promoAdapter = PromoAdapter(promos)
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
