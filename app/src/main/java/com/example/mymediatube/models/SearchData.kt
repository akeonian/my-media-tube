package com.example.mymediatube.models

import android.net.Uri
import androidx.recyclerview.widget.DiffUtil

data class SearchData(
    val id: String,
    val title: String,
    val thumbnail: Uri
) {

    companion object {
        val diffCallback = object: DiffUtil.ItemCallback<SearchData>() {
            override fun areItemsTheSame(oldItem: SearchData, newItem: SearchData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SearchData, newItem: SearchData): Boolean {
                return oldItem == newItem
            }

        }
    }
}
