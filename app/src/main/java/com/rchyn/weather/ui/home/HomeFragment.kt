package com.rchyn.weather.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.rchyn.weather.domain.model.WeatherData
import com.rchyn.weather.ui.MainActivity
import com.rchyn.weather.utils.formatedTime
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

                            state.weatherInfo.weatherDataPerDay[0]?.let { weathersData ->
                                listWeatherAdapter.submitList(weathersData)
                            }
                        }
                    }
                }
            }
        }

        setupRecyclerWeathers()
    }


    private fun setupCurrentWeather(weatherData: WeatherData) {
        binding.cardCurrentWeather.apply {
            tvDate.text = getString(R.string.today, weatherData.time.formatedTime())
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
                    requireContext().getLocationName(weatherData.latitude, weatherData.longitude).isNotEmpty()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}