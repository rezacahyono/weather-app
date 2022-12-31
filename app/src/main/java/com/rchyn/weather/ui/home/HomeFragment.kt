package com.rchyn.weather.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.rchyn.weather.R
import com.rchyn.weather.adapter.ListWeatherAdapter
import com.rchyn.weather.databinding.FragmentHomeBinding
import com.rchyn.weather.domain.model.weather.Forecast
import com.rchyn.weather.domain.model.weather.WeatherData
import com.rchyn.weather.ui.MainActivity
import com.rchyn.weather.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalTime

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding

    private val weatherViewModel: WeatherViewModel by activityViewModels()

    private lateinit var act: MainActivity
    private var weathersData: List<WeatherData> = emptyList()
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

        binding.layoutToolbarHome.root.addLiftOnScrollListener { _, backgroundColor ->
            act.window.statusBarColor = backgroundColor
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherViewModel.weatherState.collect { state ->
                    when {
//                        state.isLoading -> setupShimmerLoading(true)
//                        state.isError -> setupShimmerLoading(false)
                        state.weatherInfo != null -> {
//                            setupShimmerLoading(false)
                            state.weatherInfo.currentWeatherData?.let { weatherData ->
                                setupCurrentWeather(weatherData = weatherData)
                            }

                            state.weatherInfo.weatherDataByDay.values.forEach { weathers ->
                                weathersData = weathers
                                setupLineChartForecast(weathers, weatherViewModel.forecast.value)
                            }

                            listWeatherAdapter.submitList(state.weatherInfo.forecastWeatherData.values.toList())
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

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherViewModel.forecast.collect {
                    setupLineChartForecast(weathersData, it)
                }
            }
        }

        setupRecyclerForecastWeathers()

        binding.btnGrpForecastToday.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btn_feels_like -> weatherViewModel.forecast.value = Forecast.FEELS_LIKE
                    R.id.btn_wind_speed -> weatherViewModel.forecast.value = Forecast.WIND_SPEED
                    R.id.btn_humidity -> weatherViewModel.forecast.value = Forecast.HUMIDITY
                }
            }
        }
    }

    private fun setupCurrentWeather(weatherData: WeatherData) {
        binding.layoutToolbarHome.toolbar.apply {
            title = act.getLocationName(weatherData.latitude, weatherData.longitude)
            subtitle = act.getLocationName(weatherData.latitude, weatherData.longitude, true)
        }

        binding.tvDate.text = weatherData.time.formattedDate()
        binding.tvFilterDay.setOnClickListener { v ->
            setupPopupMenuWeatherByDay(v)
        }

        binding.layoutCardCurrentWeather.apply {
            ivIconTemperature.setImageResource(weatherData.weatherType.iconRes)
            tvTemperature.text =
                getString(R.string.temp, weatherData.temparatureCelsius.toString().cleanZero())
            tvDescTemperature.text = getString(weatherData.weatherType.weatherDesc)
        }

        binding.menuFeelsLike.apply {
            ivIconUtilities.setImageResource(R.drawable.ic_temp)
            tvTitleUtilities.text = getString(R.string.feels_like)
            tvUtilities.text =
                getString(R.string.temp, weatherData.feelsLikeCelsius.toString().cleanZero())
        }

        binding.menuWindSpeed.apply {
            ivIconUtilities.setImageResource(R.drawable.ic_wind)
            tvTitleUtilities.text = getString(R.string.wind_speed)
            tvUtilities.text =
                getString(R.string.speed, weatherData.windSpeed.toString().cleanZero())
        }

        binding.menuHummidity.apply {
            ivIconUtilities.setImageResource(R.drawable.ic_humidity)
            tvTitleUtilities.text = getString(R.string.humidity)

            @SuppressLint("StringFormatInvalid")
            tvUtilities.text =
                getString(R.string.percent, weatherData.humidity.toString().cleanZero())
        }

    }

    private fun setupRecyclerForecastWeathers() {
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerForecast.apply {
            layoutManager = linearLayoutManager
            adapter = listWeatherAdapter
        }
    }

    private fun setupPopupMenuWeatherByDay(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.apply {
            menuInflater.inflate(R.menu.weather_by_day_menu, popupMenu.menu)
            setOnMenuItemClickListener { menuItem: MenuItem ->
                weatherViewModel.day.value = when (menuItem.itemId) {
                    R.id.today -> 0
                    R.id.tomorrow -> 1
                    R.id.two_day_ago -> 2
                    R.id.three_day_ago -> 3
                    R.id.four_day_ago -> 4
                    R.id.five_day_ago -> 5
                    R.id.six_day_ago -> 6
                    else -> 0
                }
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
        binding.tvFilterDay.text = getString(titleDay)
    }

    private fun setupLineChartForecast(
        weathers: List<WeatherData>,
        forecast: Forecast
    ) {
        if (weathers.isEmpty()) return
        val entry = weathers.map {
            Entry(
                it.time.hour.toFloat(),
                when (forecast) {
                    Forecast.FEELS_LIKE -> it.temparatureCelsius.toFloat()
                    Forecast.WIND_SPEED -> it.windSpeed.toFloat()
                    Forecast.HUMIDITY -> it.humidity.toFloat()
                },
                when (forecast) {
                    Forecast.FEELS_LIKE -> ContextCompat.getDrawable(
                        requireContext(),
                        it.weatherType.iconResSmall
                    )
                    Forecast.WIND_SPEED -> ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_wind
                    )
                    Forecast.HUMIDITY -> ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_humidity
                    )
                }
            )
        }

        val lineData = LineDataSet(entry, "").apply {
            styleLineDataSet(this, forecast)
        }.let {
            LineData(it)
        }

        binding.lineChartForecast.apply {
            data = lineData
            styleLineChart(this)
            invalidate()
        }
    }

    private fun styleLineChart(lineChart: LineChart) = lineChart.apply {
        extraRightOffset = 18f
        extraLeftOffset = 18f
        extraTopOffset = 45f
        axisLeft.isEnabled = false
        axisRight.isEnabled = false

        val timeValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val localTime = LocalTime.of(value.toInt(), 0)
                return localTime.formatterTimeNow(requireContext())
            }
        }

        xAxis.apply {
            axisMaximum = 23f
            axisMinimum = 0f
            setDrawGridLines(false)
            setDrawAxisLine(false)
            position = XAxis.XAxisPosition.BOTTOM
            textColor =
                requireContext().resolveColorAttr(com.google.android.material.R.attr.colorOnSurface)
            valueFormatter = timeValueFormatter
        }

        setTouchEnabled(true)
        isDragEnabled = true
        setScaleEnabled(false)
        setPinchZoom(false)
        setScaleMinima(4f, 0f)
        description = null
        legend.isEnabled = false
        animateX(500)
    }

    private fun styleLineDataSet(lineDataSet: LineDataSet, forecast: Forecast) = lineDataSet.apply {
        color = requireContext().resolveColorAttr(androidx.appcompat.R.attr.colorPrimary)
        valueTextColor =
            requireContext().resolveColorAttr(com.google.android.material.R.attr.colorOnSurface)

        valueTextSize = 12f


        val tempValueFormatter = object : ValueFormatter() {
            @SuppressLint("StringFormatInvalid")
            override fun getFormattedValue(value: Float): String {
                return when (forecast) {
                    Forecast.FEELS_LIKE -> getString(R.string.temp, value.toString().cleanZero())
                    Forecast.WIND_SPEED -> value.toString()
                    Forecast.HUMIDITY -> getString(R.string.percent, value.toString())
                }
            }
        }

        valueFormatter = tempValueFormatter

        iconsOffset = MPPointF(0f, -40f)
        lineWidth = 2f
        isHighlightEnabled = true
        setDrawHorizontalHighlightIndicator(false)
        setDrawVerticalHighlightIndicator(false)
        setDrawCircles(true)
        setDrawCircleHole(false)
        setCircleColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
        circleRadius = 6f
        mode = LineDataSet.Mode.CUBIC_BEZIER

        setDrawFilled(true)
        fillDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.shape_bg_filled_line_chart)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}