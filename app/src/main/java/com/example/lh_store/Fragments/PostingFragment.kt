package com.example.lh_store.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lh_store.Api.ApiConfig
import com.example.lh_store.Models.PostProductRequest
import com.example.lh_store.Models.PostProductResponse
import com.example.lh_store.R
import com.example.lh_store.databinding.FragmentPostingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostingFragment : Fragment() {

    private var _binding: FragmentPostingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener {
            postProduct()
        }
    }

    private fun postProduct() {
        val productName = binding.postName.text.toString().trim()
        val productImage = binding.postImgUrl.text.toString().trim()
        val productOs = binding.postOs.text.toString().trim()
        val productPriceStr = binding.postPrice.text.toString().trim()
        val productBattery = binding.postBatteryCapacity.text.toString().trim()
        val productCategory = binding.postCategory.text.toString().trim()
        val productRam = binding.postRam.text.toString().trim()
        val productReleasedStr = binding.postReleasedYear.text.toString().trim()

        // Validasi input kosong
        if (productName.isEmpty() || productImage.isEmpty() || productOs.isEmpty() ||
            productPriceStr.isEmpty() || productBattery.isEmpty() ||
            productCategory.isEmpty() || productRam.isEmpty() || productReleasedStr.isEmpty()) {
            Toast.makeText(requireContext(), "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        // Konversi ke Int dengan error handling
        val productPrice = productPriceStr.toIntOrNull() ?: run {
            binding.postPrice.error = "Harga harus angka"
            return
        }

        val productReleased = productReleasedStr.toIntOrNull() ?: run {
            binding.postReleasedYear.error = "Tahun harus angka"
            return
        }

        // Membuat request object
        val productRequest = PostProductRequest(
            image = productImage,
            os = productOs,
            price = productPrice,
            name = productName,
            battery = productBattery,
            category = productCategory,
            released = productReleased.toString(),
            ram = productRam
        )

        // Memanggil API
        ApiConfig.instance.postProduct(productRequest).enqueue(object : Callback<PostProductResponse> {
            override fun onResponse(
                call: Call<PostProductResponse>,
                response: Response<PostProductResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Produk berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    // Kembali ke fragment sebelumnya
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Gagal: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PostProductResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        // Sembunyikan SearchView ketika fragment ini aktif
        hideSearchView()
    }

    override fun onPause() {
        super.onPause()
        // Tampilkan kembali SearchView ketika meninggalkan fragment ini
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