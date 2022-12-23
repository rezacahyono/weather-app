package com.rchyn.weather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rchyn.weather.databinding.ItemRowLocationBinding
import com.rchyn.weather.domain.model.location.LocationData

class ListLocationAdapter(
    private val onClickItem: (LocationData) -> Unit
) : ListAdapter<LocationData, ListLocationAdapter.ListLocationViewHolder>(DiffCallback) {
    inner class ListLocationViewHolder(
        private val binding: ItemRowLocationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(locationData: LocationData) {
            binding.tvTitleLocation.text = locationData.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListLocationViewHolder {
        val binding =
            ItemRowLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListLocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListLocationViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    private companion object DiffCallback : DiffUtil.ItemCallback<LocationData>() {
        override fun areItemsTheSame(oldItem: LocationData, newItem: LocationData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: LocationData, newItem: LocationData): Boolean {
            return oldItem.name == newItem.name
        }

    }
}