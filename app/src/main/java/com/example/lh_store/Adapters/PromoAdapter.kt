package com.example.lh_store.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lh_store.Models.ResponsePromo
import com.example.lh_store.databinding.PromoItemBinding

class PromoAdapter (
    private val promos: List<ResponsePromo>
) : RecyclerView.Adapter<PromoAdapter.PromoViewHolder>() {
    inner class PromoViewHolder(val binding: PromoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoViewHolder {
        val binding = PromoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PromoViewHolder(binding)
    }

    override fun getItemCount(): Int = promos.size

    override fun onBindViewHolder(holder: PromoViewHolder, position: Int) {
        val promo = promos[position]

        holder.binding.apply {
            descPromo.text = promo.description
            Glide.with(imagePromo.context)
                .load(promo.image)
                .into(imagePromo)
        }
    }

}