package com.example.lh_store.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.lh_store.R
import com.example.lh_store.ViewModels.ProductViewModel
import com.example.lh_store.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!
    private val productViewModel: ProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)

        val call = binding.callService
        val mail = binding.mailService

        // Observe product count
        productViewModel.productCount.observe(viewLifecycleOwner) { count ->
            binding.amountProduct.text = count.toString()
        }
        // Observe promo count
        productViewModel.promoCount.observe(viewLifecycleOwner) { count ->
            binding.amountActivePromo.text = count.toString()
        }

        call.setOnClickListener {
            val tel = "1234567890"
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$tel"))
            startActivity(intent)
        }

        mail.setOnClickListener {
            val email = "example@mail.com"
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
            startActivity(intent)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        hideSearchView()
    }

    override fun onPause() {
        super.onPause()
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
