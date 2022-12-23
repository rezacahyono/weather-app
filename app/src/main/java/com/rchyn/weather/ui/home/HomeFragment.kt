package com.rchyn.weather.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.rchyn.weather.R
import com.rchyn.weather.adapter.ListWeatherAdapter
import com.rchyn.weather.databinding.FragmentHomeBinding
import com.rchyn.weather.domain.model.weather.WeatherData
import com.rchyn.weather.ui.MainActivity
import com.rchyn.weather.utils.formattedDate
import com.rchyn.weather.utils.getLocationName
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding

    private val weatherViewModel: WeatherViewModel by activityViewModels()

    private lateinit var act: MainActivity

    private lateinit var listWeatherAdapter: ListWeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        act = activity as MainActivity
        listWeatherAdapter = ListWeatherAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherViewModel.weatherState.collect { state ->
                    when {
                        state.isLoading -> setupShimmerCardLoading(true)
                        state.isError -> setupShimmerCardLoading(false)
                        state.weatherInfo != null -> {
                            state.weatherInfo.currentWeatherData?.let { weatherData ->
                                setupShimmerCardLoading(false)
                                setupCurrentWeather(weatherData = weatherData)
                            }

                            state.weatherInfo.weatherDataPerDay.values.forEach {

                                listWeatherAdapter.submitList(it)
                            }
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherViewModel.day.collect {
                    changeTitleDay(it)
                }
            }
        }
        setupRecyclerWeathers()

        binding.layoutHomeToolbar.tvPickForecastDay.setOnClickListener { v ->
            setupPopMenuForecastByDay(v)
        }
    }


    private fun setupCurrentWeather(weatherData: WeatherData) {
        binding.cardCurrentWeather.apply {
            tvDate.text = getString(R.string.time, weatherData.time.formattedDate())
            ivIconTemperature.setImageResource(weatherData.weatherType.iconRes)
            tvTemperature.text = getString(R.string.temp, weatherData.temparatureCelsius.toString())
            tvWeatherDesc.text = getString(weatherData.weatherType.weatherDesc)
            tvFeelsLike.text =
                getString(R.string.feels_like, weatherData.feelsLikeCelsius.toInt().toString())
            tvWindSpeed.text = getString(R.string.wind_speed, weatherData.windSpeed.toString())
            @SuppressLint("StringFormatInvalid")
            tvHumidity.text = getString(R.string.humidity, weatherData.humidity.toInt().toString())

            tvLocationName.apply {
                text = requireContext().getLocationName(weatherData.latitude, weatherData.longitude)
                isVisible =
                    requireContext().getLocationName(weatherData.latitude, weatherData.longitude)
                        .isNotEmpty()
            }
        }
    }

    private fun setupRecyclerWeathers() {
        val gridLayoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        binding.recyclerWeather.apply {
            layoutManager = gridLayoutManager
            adapter = listWeatherAdapter
        }
    }


    private fun setupShimmerCardLoading(isShow: Boolean) {
        binding.shimmerCardWeather.apply {
            if (isShow) startShimmer() else stopShimmer()
            isVisible = isShow
            binding.cardCurrentWeather.root.isVisible = !isShow
        }
    }

    private fun setupPopMenuForecastByDay(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.forecast_day_menu, popupMenu.menu)

        popupMenu.apply {
            setOnMenuItemClickListener { menuItem: MenuItem ->
                val day: Int = when (menuItem.itemId) {
                    R.id.today -> 0
                    R.id.tomorrow -> 1
                    R.id.two_day_ago -> 2
                    R.id.three_day_ago -> 3
                    R.id.four_day_ago -> 4
                    R.id.five_day_ago -> 5
                    R.id.six_day_ago -> 6
                    else -> 0
                }
                weatherViewModel.day.value = day
                weatherViewModel.loadWeatherByCurrentLocation()
                true
            }
            show()
        }
    }

    private fun changeTitleDay(value: Int) {
        val titleDay = when (value) {
            1 -> R.string.tomorrow
            2 -> R.string.two_day_ago
            3 -> R.string.three_day_ago
            4 -> R.string.four_day_ago
            5 -> R.string.five_day_ago
            6 -> R.string.six_day_ago
            else -> R.string.today
        }
        binding.layoutHomeToolbar.tvPickForecastDay.text = getString(titleDay)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}