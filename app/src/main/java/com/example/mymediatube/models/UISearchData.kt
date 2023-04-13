package com.example.mymediatube.models

import android.net.Uri
import androidx.recyclerview.widget.DiffUtil

data class UISearchData(
    val id: String,
    val title: String,
    val thumbnail: Uri,
    val dataUri: Uri
) {

    companion object {
        val diffCallback = object: DiffUtil.ItemCallback<UISearchData>() {
            override fun areItemsTheSame(oldItem: UISearchData, newItem: UISearchData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UISearchData, newItem: UISearchData): Boolean {
                return oldItem == newItem
            }

        }
    }
}
