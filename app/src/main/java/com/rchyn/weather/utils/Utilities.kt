package com.rchyn.weather.utils

import android.content.Context
import android.icu.util.Calendar
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.core.view.isVisible
import com.rchyn.weather.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


fun checkNight(): Boolean {
    val calendar = Calendar.getInstance()
    return when (calendar.get(Calendar.HOUR_OF_DAY)) {
        in 0..4 -> true
        in 5..17 -> false
        in 18..23 -> true
        else -> false
    }
}

fun LocalDateTime.formatedTime(formated: String = "HH MMM yyyy"): String {
    return this.format(DateTimeFormatter.ofPattern(formated))
}

fun String.cleanZero(): String {
    return this.replaceFirstChar { if (it == '0') "" else "$it" }
}

fun Context.getLocationName(lat: Double, lon: Double): String {
    val localeId = Locale("id", "ID")
    var result = ""
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val geocoder = Geocoder(this, localeId)
        val geocodeListener = object : Geocoder.GeocodeListener {
            override fun onGeocode(addresses: MutableList<Address>) {
                result = getString(
                    R.string.location_name,
                    addresses[0].subAdminArea,
                    addresses[0].countryName
                )
            }

            override fun onError(errorMessage: String?) {
                super.onError(errorMessage)
                result = ""
            }

        }
        geocoder.getFromLocation(lat, lon, 1, geocodeListener)
    } else {
        result = try {
            val geocoder = Geocoder(this, localeId)

            @Suppress("DEPRECATION")
            val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1) as List<Address>

            getString(
                R.string.location_name,
                addresses[0].subAdminArea,
                addresses[0].countryName
            )
        } catch (e: Exception) {
            ""
        }
    }
    return result
}