package com.rchyn.weather.domain.model.weather

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.rchyn.weather.R
import com.rchyn.weather.utils.checkNight

sealed class WeatherType(
    @DrawableRes
    val iconRes: Int,
    @DrawableRes
    val iconResSmall: Int,
    @StringRes
    val weatherDesc: Int
) {
    object ClearSky : WeatherType(
        weatherDesc = R.string.clear_sky,
        iconRes = if (checkNight()) R.drawable.night_moon else R.drawable.day_sun,
        iconResSmall = if (checkNight()) R.drawable.ic_night_moon else R.drawable.ic_day_sun
    )

    object MainlyClear : WeatherType(
        weatherDesc = R.string.mainly_clear,
        iconRes = if (checkNight()) R.drawable.night_moon else R.drawable.day_sun,
        iconResSmall = if (checkNight()) R.drawable.ic_night_moon else R.drawable.ic_day_sun
    )

    object PartlyCloudy : WeatherType(
        weatherDesc = R.string.partly_cloudy,
        iconRes = if (checkNight()) R.drawable.night_clouds else R.drawable.day_clouds,
        iconResSmall = if (checkNight()) R.drawable.ic_night_clouds else R.drawable.ic_day_clouds
    )

    object Overcast : WeatherType(
        weatherDesc = R.string.over_cast,
        iconRes = if (checkNight()) R.drawable.night_clouds else R.drawable.day_clouds,
        iconResSmall = if (checkNight()) R.drawable.ic_night_clouds else R.drawable.ic_day_clouds
    )

    object Foggy : WeatherType(
        weatherDesc = R.string.foggy,
        iconRes = if (checkNight()) R.drawable.night_wind else R.drawable.day_wind,
        iconResSmall = if (checkNight()) R.drawable.ic_night_wind else R.drawable.ic_day_wind
    )

    object DepositingRimeFog : WeatherType(
        weatherDesc = R.string.depositing_rime_fog,
        iconRes = if (checkNight()) R.drawable.night_wind else R.drawable.day_wind,
        iconResSmall = if (checkNight()) R.drawable.ic_night_wind else R.drawable.ic_day_wind
    )

    object LightDrizzle : WeatherType(
        weatherDesc = R.string.light_drizzle,
        iconRes = if (checkNight()) R.drawable.night_rain else R.drawable.day_rain,
        iconResSmall = if (checkNight()) R.drawable.ic_night_rain else R.drawable.ic_day_rain
    )

    object ModerateDrizzle : WeatherType(
        weatherDesc = R.string.moderate_drizzle,
        iconRes = if (checkNight()) R.drawable.night_rain else R.drawable.day_rain,
        iconResSmall = if (checkNight()) R.drawable.ic_night_rain else R.drawable.ic_day_rain
    )

    object DenseDrizzle : WeatherType(
        weatherDesc = R.string.dense_drizzle,
        iconRes = if (checkNight()) R.drawable.night_rain else R.drawable.day_rain,
        iconResSmall = if (checkNight()) R.drawable.ic_night_rain else R.drawable.ic_day_rain
    )

    object LightFreezingDrizzle : WeatherType(
        weatherDesc = R.string.light_freezing_drizzle,
        iconRes = if (checkNight()) R.drawable.night_snow else R.drawable.day_snow,
        iconResSmall = if (checkNight()) R.drawable.ic_night_snow else R.drawable.ic_day_snow
    )

    object DenseFreezingDrizzle : WeatherType(
        weatherDesc = R.string.dense_freezing_drizzle,
        iconRes = if (checkNight()) R.drawable.night_snow else R.drawable.day_snow,
        iconResSmall = if (checkNight()) R.drawable.ic_night_snow else R.drawable.ic_day_snow
    )

    object LightRain : WeatherType(
        weatherDesc = R.string.light_rain,
        iconRes = if (checkNight()) R.drawable.night_rain else R.drawable.day_rain,
        iconResSmall = if (checkNight()) R.drawable.ic_night_rain else R.drawable.ic_day_rain
    )

    object ModerateRain : WeatherType(
        weatherDesc = R.string.moderate_rain,
        iconRes = if (checkNight()) R.drawable.night_rain else R.drawable.day_rain,
        iconResSmall = if (checkNight()) R.drawable.ic_night_rain else R.drawable.ic_day_rain
    )

    object HeavyRain : WeatherType(
        weatherDesc = R.string.heavy_rain,
        iconRes = if (checkNight()) R.drawable.night_rain else R.drawable.day_rain,
        iconResSmall = if (checkNight()) R.drawable.ic_night_rain else R.drawable.ic_day_rain
    )

    object HeavyFreezingRain : WeatherType(
        weatherDesc = R.string.heavy_freezing_rain,
        iconRes = if (checkNight()) R.drawable.night_snow else R.drawable.day_snow,
        iconResSmall = if (checkNight()) R.drawable.ic_night_snow else R.drawable.ic_day_snow
    )

    object SlightSnowFall : WeatherType(
        weatherDesc = R.string.light_snow_fall,
        iconRes = if (checkNight()) R.drawable.night_snow else R.drawable.day_snow,
        iconResSmall = if (checkNight()) R.drawable.ic_night_snow else R.drawable.ic_day_snow
    )

    object ModerateSnowFall : WeatherType(
        weatherDesc = R.string.moderate_snow_fall,
        iconRes = if (checkNight()) R.drawable.night_snow else R.drawable.day_snow,
        iconResSmall = if (checkNight()) R.drawable.ic_night_snow else R.drawable.ic_day_snow
    )

    object HeavySnowFall : WeatherType(
        weatherDesc = R.string.heavy_snow_fall,
        iconRes = if (checkNight()) R.drawable.night_snow else R.drawable.day_snow,
        iconResSmall = if (checkNight()) R.drawable.ic_night_snow else R.drawable.ic_day_snow
    )

    object SnowGrains : WeatherType(
        weatherDesc = R.string.snow_grains,
        iconRes = if (checkNight()) R.drawable.night_snow else R.drawable.day_snow,
        iconResSmall = if (checkNight()) R.drawable.ic_night_snow else R.drawable.ic_day_snow
    )

    object LightRainShowers : WeatherType(
        weatherDesc = R.string.light_rain_showers,
        iconRes = if (checkNight()) R.drawable.night_rain else R.drawable.day_rain,
        iconResSmall = if (checkNight()) R.drawable.ic_night_rain else R.drawable.ic_day_rain
    )

    object ModerateRainShowers : WeatherType(
        weatherDesc = R.string.moderate_rain_showers,
        iconRes = if (checkNight()) R.drawable.night_rain else R.drawable.day_rain,
        iconResSmall = if (checkNight()) R.drawable.ic_night_rain else R.drawable.ic_day_rain
    )

    object ViolentRainShowers : WeatherType(
        weatherDesc = R.string.violent_rain_showers,
        iconRes = if (checkNight()) R.drawable.night_rain else R.drawable.day_rain,
        iconResSmall = if (checkNight()) R.drawable.ic_night_rain else R.drawable.ic_day_rain
    )

    object LightSnowShowers : WeatherType(
        weatherDesc = R.string.ligth_snow_showers,
        iconRes = if (checkNight()) R.drawable.night_snow else R.drawable.day_snow,
        iconResSmall = if (checkNight()) R.drawable.ic_night_snow else R.drawable.ic_day_snow
    )

    object HeavySnowShowers : WeatherType(
        weatherDesc = R.string.heavy_snow_showers,
        iconRes = if (checkNight()) R.drawable.night_snow else R.drawable.day_snow,
        iconResSmall = if (checkNight()) R.drawable.ic_night_snow else R.drawable.ic_day_snow
    )

    object ModerateThunderstorm : WeatherType(
        weatherDesc = R.string.moderate_thunder_storm,
        iconRes = if (checkNight()) R.drawable.night_storm else R.drawable.day_storm,
        iconResSmall = if (checkNight()) R.drawable.ic_night_storm else R.drawable.ic_day_storm
    )

    object LightHailThunderstorm : WeatherType(
        weatherDesc = R.string.light_hail_thunder_storm,
        iconRes = if (checkNight()) R.drawable.night_storm else R.drawable.day_storm,
        iconResSmall = if (checkNight()) R.drawable.ic_night_storm else R.drawable.ic_day_storm
    )

    object HeavyHailThunderstorm : WeatherType(
        weatherDesc = R.string.heavy_hail_thunder_storm,
        iconRes = if (checkNight()) R.drawable.night_storm else R.drawable.day_storm,
        iconResSmall = if (checkNight()) R.drawable.ic_night_storm else R.drawable.ic_day_storm
    )

    companion object {
        fun fromWMO(code: Int): WeatherType {
            return when (code) {
                0 -> ClearSky
                1 -> MainlyClear
                2 -> PartlyCloudy
                3 -> Overcast
                45 -> Foggy
                48 -> DepositingRimeFog
                51 -> LightDrizzle
                53 -> ModerateDrizzle
                55 -> DenseDrizzle
                56 -> LightFreezingDrizzle
                57 -> DenseFreezingDrizzle
                61 -> LightRain
                63 -> ModerateRain
                65 -> HeavyRain
                66 -> LightFreezingDrizzle
                67 -> HeavyFreezingRain
                71 -> SlightSnowFall
                73 -> ModerateSnowFall
                75 -> HeavySnowFall
                77 -> SnowGrains
                80 -> LightRainShowers
                81 -> ModerateRainShowers
                82 -> ViolentRainShowers
                85 -> LightSnowShowers
                86 -> HeavySnowShowers
                95 -> ModerateThunderstorm
                96 -> LightHailThunderstorm
                99 -> HeavyHailThunderstorm
                else -> ClearSky
            }
        }
    }
}
