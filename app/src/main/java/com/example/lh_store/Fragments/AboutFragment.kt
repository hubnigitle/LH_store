package com.example.lh_store.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.lh_store.R
import com.example.lh_store.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root

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
