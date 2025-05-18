package com.example.lh_store.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.lh_store.Models.ResponseProduct
import com.example.lh_store.databinding.FragmentProductDetailBinding

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var product: ResponseProduct

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        product = arguments?.getParcelable("product")!!

        binding.apply {
            nameProduct.text = product.name
            priceProduct.text = "Price: IDR${product.price}"
            osProduct.text = "OS: ${product.os}"
            ramProduct.text = "RAM: ${product.ram}"
            batteryProduct.text = "Battery: ${product.battery}"
            categoryProduct.text = "Category: ${product.category}"
            releasedProduct.text = "Released: ${product.released}"

            // Tampilkan gambar produk
            Glide.with(imgProduct.context)
                .load(product.image)
                .into(imgProduct)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
