package com.example.lh_store.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.collection.intLongMapOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.lh_store.Api.ApiConfig
import com.example.lh_store.Models.ResponseProduct
import com.example.lh_store.Models.DeleteProductResponse
import com.example.lh_store.Models.PostProductRequest
import com.example.lh_store.Models.PostProductResponse
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
    private var isEditMode: Boolean = false

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

        binding.btnEdit.setOnClickListener {
            toggleEditMode()
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

    private fun toggleEditMode() {
        isEditMode = !isEditMode
        with(binding) {
            if (isEditMode) {
                // Switch to edit mode - hide TextViews, show EditTexts
                listOf(
                    nameProduct, priceProduct, osProduct, ramProduct,
                    batteryProduct, categoryProduct, releasedProduct
                ).forEach { it.visibility = View.GONE }

                val fields = listOf(
                    imgProductEdit to product.image,
                    nameProductEdit to product.name,
                    priceProductEdit to product.price.toString(),
                    osProductEdit to product.os,
                    ramProductEdit to product.ram,
                    batteryProductEdit to product.battery,
                    categoryProductEdit to product.category,
                    releasedProductEdit to product.released.toString()
                )

                fields.forEach { (view, value) ->
                    view.visibility = View.VISIBLE
                    view.setText(value)
                }

                btnDelete.visibility = View.GONE
                btnEdit.text = "Save"
            } else {
                // Switch back to view mode
                updateProduct()
            }
        }
    }

    private fun updateProduct() {
        with(binding) {
            val updatedImg = imgProductEdit.text.toString().trim()
            val updatedName = nameProductEdit.text.toString().trim()
            val updatedPrice = priceProductEdit.text.toString().trim()
            val updatedOs = osProductEdit.text.toString().trim()
            val updatedRam = ramProductEdit.text.toString().trim()
            val updatedBattery = batteryProductEdit.text.toString().trim()
            val updatedCategory = categoryProductEdit.text.toString().trim()
            val updatedReleased = releasedProductEdit.text.toString().trim()

            // Validate all fields
            if (updatedImg.isEmpty() || updatedName.isEmpty() || updatedPrice.isEmpty() || updatedOs.isEmpty() ||
                updatedRam.isEmpty() || updatedBattery.isEmpty() || updatedCategory.isEmpty() ||
                updatedReleased.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return
            }

            // Validate numeric fields
            val priceValue = updatedPrice.toIntOrNull()
            val releasedValue = updatedReleased.toIntOrNull()

            if (priceValue == null) {
                Toast.makeText(requireContext(), "Price must be a whole number (e.g., 100)", Toast.LENGTH_SHORT).show()
                return
            }

            if (releasedValue == null) {
                Toast.makeText(requireContext(), "Released year must be a whole number (e.g., 2023)", Toast.LENGTH_SHORT).show()
                return
            }

            val updateRequest = PostProductRequest(
                image = updatedImg,
                name = updatedName,
                price = priceValue,
                os = updatedOs,
                ram = updatedRam,
                battery = updatedBattery,
                category = updatedCategory,
                released = releasedValue
            )

            ApiConfig.instance.updateProduct(id = product.id.toString(), product = updateRequest)
                .enqueue(object : Callback<PostProductResponse> {
                    override fun onResponse(
                        call: Call<PostProductResponse>,
                        response: Response<PostProductResponse>
                    ) {
                        if (response.isSuccessful) {
                            // Update UI with new data
                            Glide.with(requireContext())
                                .load(updatedImg)
                                .into(imgProduct)

                            nameProduct.text = updatedName
                            priceProduct.text = updatedPrice
                            osProduct.text = updatedOs
                            ramProduct.text = updatedRam
                            batteryProduct.text = updatedBattery
                            categoryProduct.text = updatedCategory
                            releasedProduct.text = updatedReleased

                            // Switch back to view mode
                            listOf(
                                imgProduct,nameProduct, priceProduct, osProduct, ramProduct,
                                batteryProduct, categoryProduct, releasedProduct
                            ).forEach { it.visibility = View.VISIBLE }

                            listOf(
                                imgProductEdit,nameProductEdit, priceProductEdit, osProductEdit, ramProductEdit,
                                batteryProductEdit, categoryProductEdit, releasedProductEdit
                            ).forEach { it.visibility = View.GONE }

                            btnDelete.visibility = View.VISIBLE
                            btnEdit.text = "Edit"

                            Toast.makeText(requireContext(), "Product updated successfully", Toast.LENGTH_SHORT).show()
                            productViewModel.notifyProductUpdated()
                        } else {
                            Toast.makeText(requireContext(), "Failed to update product", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<PostProductResponse>, t: Throwable) {
                        Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
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
