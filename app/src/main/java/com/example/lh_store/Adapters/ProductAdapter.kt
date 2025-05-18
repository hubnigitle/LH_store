package com.example.lh_store.Adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lh_store.Fragments.ProductDetailFragment
import com.example.lh_store.MainActivity
import com.example.lh_store.Models.ResponseProduct
import com.example.lh_store.databinding.ProductItemBinding

class ProductAdapter(
    private val products: List<ResponseProduct>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.binding.apply {
            nameProduct.text = product.name
            priceProduct.text = "Price: IDR${product.price}"
            Glide.with(imgProduct.context)
                .load(product.image)
                .into(imgProduct)

            root.setOnClickListener {
                val bundle = Bundle().apply {
                    putParcelable("product", product)
                }
                val fragment = ProductDetailFragment().apply {
                    arguments = bundle
                }
                (root.context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(com.example.lh_store.R.id.nav_host_fragment, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun getItemCount(): Int = products.size
}
