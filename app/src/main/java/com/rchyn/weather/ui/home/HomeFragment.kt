package com.rchyn.weather.ui.home

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.Geocoder.GeocodeListener
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rchyn.weather.R
import com.rchyn.weather.databinding.FragmentHomeBinding
import com.rchyn.weather.domain.model.WeatherData
import com.rchyn.weather.utils.formatedTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding

    private val weatherViewModel: WeatherViewModel by viewModels()

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
                    state.weatherInfo?.let {
                        it.currentWeatherData?.let { weatherData ->
                            setupCurrentWeather(weatherData = weatherData)
                        }
                    }
                }
            }
        }
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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val geocoder = Geocoder(requireContext(), Locale("id", "ID"))

                val geocodeListener = object : GeocodeListener {
                    override fun onGeocode(addresses: MutableList<Address>) {
                        tvLocationName.text = getString(
                            R.string.location_name,
                            addresses[0].subAdminArea,
                            addresses[0].countryName
                        )
                    }

                    override fun onError(errorMessage: String?) {
                        super.onError(errorMessage)
                        tvLocationName.isVisible = false
                    }

                }
                geocoder.getFromLocation(
                    weatherData.latitude,
                    weatherData.longitude,
                    1,
                    geocodeListener
                )
            }

            tvLocationName.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}