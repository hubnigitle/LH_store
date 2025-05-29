package com.example.lh_store.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.lh_store.Api.ApiConfig
import com.example.lh_store.Models.ResponseProduct
import com.example.lh_store.Models.DeleteProductResponse
import com.example.lh_store.R
import com.example.lh_store.ViewModels.ProductViewModel
import com.example.lh_store.databinding.FragmentProductDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailFragment : Fragment() {
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var product: ResponseProduct
    private val productViewModel: ProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        product = arguments?.getParcelable("product") ?: return

        binding.apply {
            nameProduct.text = product.name
            priceProduct.text = "Price: IDR${product.price}"
            osProduct.text = "OS: ${product.os}"
            ramProduct.text = "RAM: ${product.ram}"
            batteryProduct.text = "Battery: ${product.battery}"
            categoryProduct.text = "Category: ${product.category}"
            releasedProduct.text = "Released: ${product.released}"

            // Display product image
            Glide.with(imgProduct.context)
                .load(product.image)
                .into(imgProduct)
        }

        binding.btnDelete.setOnClickListener {

            val dialog = AlertDialog.Builder(requireContext())

            dialog.setTitle("Delete product")
            dialog.setMessage("Do you want to delete this product?")
            dialog.setIcon(R.drawable.ic_delete)

            dialog.setPositiveButton("Yes") { _, _ ->
                product.id?.let { it1 -> deleteProduct(it1) }
            }

            dialog.setNeutralButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }

            val alertDialog: AlertDialog = dialog.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }

    private fun deleteProduct(idProduct: String) {
        ApiConfig.instance.deleteProduct(id = idProduct).enqueue(object : Callback<DeleteProductResponse> {
            override fun onResponse(
                call: Call<DeleteProductResponse>,
                response: Response<DeleteProductResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Product berhasil dihapus", Toast.LENGTH_SHORT).show()
                    productViewModel.notifyProductDeleted()
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Product gagal dihapus", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DeleteProductResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Product gagal dihapus", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        // Hide SearchView when this fragment is active
        hideSearchView()
    }

    override fun onPause() {
        super.onPause()
        // Show SearchView when leaving this fragment
        showSearchView()
    }

    private fun hideSearchView() {
        val searchView = activity?.findViewById<SearchView>(R.id.search)
        searchView?.visibility = View.GONE
    }

    private fun showSearchView() {
        val searchView = activity?.findViewById<SearchView>(R.id.search)
        searchView?.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
