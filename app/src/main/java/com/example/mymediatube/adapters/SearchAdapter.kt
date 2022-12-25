package com.example.mymediatube.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymediatube.databinding.ListItemSearchBinding
import com.example.mymediatube.models.SearchData

class SearchAdapter(
    private val clickListener: (SearchData) -> Unit
): ListAdapter<SearchData, SearchAdapter.ViewHolder> (SearchData.diffCallback) {

    class ViewHolder(
        private val binding: ListItemSearchBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchData) {
            binding.title.text = item.title
            // TODO: Configure the Glide App module
            Glide.with(binding.image)
                .load(item.thumbnail)
                .into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemSearchBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { clickListener(item) }
    }

}