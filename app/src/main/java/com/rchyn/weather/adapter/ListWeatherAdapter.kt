package com.rchyn.weather.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rchyn.weather.R
import com.rchyn.weather.databinding.ItemRowWeatherBinding
import com.rchyn.weather.domain.model.weather.WeatherData
import com.rchyn.weather.utils.formattedTimeNow

class ListWeatherAdapter :
    ListAdapter<WeatherData, ListWeatherAdapter.ListWeatherViewHolder>(DiffCallback) {

    private lateinit var ctx: Context

    inner class ListWeatherViewHolder(
        private val binding: ItemRowWeatherBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherData: WeatherData) {
            binding.apply {
                tvDate.text = weatherData.time.formattedTimeNow(context = ctx)
                ivIconTemperature.setImageResource(weatherData.weatherType.iconRes)
                tvTemperature.text =
                    ctx.getString(R.string.temp, weatherData.temparatureCelsius.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListWeatherViewHolder {
        ctx = parent.context
        val binding = ItemRowWeatherBinding.inflate(LayoutInflater.from(ctx), parent, false)
        return ListWeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListWeatherViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    private companion object DiffCallback : DiffUtil.ItemCallback<WeatherData>() {
        override fun areItemsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
            return oldItem.time == newItem.time
        }

    }
}