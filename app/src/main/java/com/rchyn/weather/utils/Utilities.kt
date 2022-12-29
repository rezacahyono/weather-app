package com.rchyn.weather.utils

import android.content.Context
import android.icu.util.Calendar
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.rchyn.weather.R
import com.rchyn.weather.utils.Constant.BASE_URL_FLAG
import com.rchyn.weather.utils.Constant.EXTENTION_SVG
import java.time.LocalDateTime
import java.time.LocalTime
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

fun LocalDateTime.formattedDate(formated: String = "EEEE, dd MMM yyyy"): String {
    return this.format(DateTimeFormatter.ofPattern(formated))
}

fun LocalDateTime.formattedDateNow(context: Context, formated: String = "EE"): String {
    val now = LocalDateTime.now()
    return if (this.dayOfWeek == now.dayOfWeek) {
        context.getString(R.string.today)
    } else {
        this.formattedDate(
            formated
        ).cleanZeroFirst()
    }
}

fun LocalTime.formatterTimeNow(context: Context, formated: String = "H:mm"): String {
    val now = LocalTime.now()
    return if (this.hour == now.hour) {
        context.getString(R.string.now)
    } else {
        this.format(DateTimeFormatter.ofPattern(formated))
    }
}


fun String.cleanZeroFirst(): String {
    return this.replaceFirstChar { if (it == '0') "" else "$it" }
}

fun String.cleanZero(): String {
    return if (this.contains(".0")) this.replace(".0", "") else this
}

fun Context.getLocationName(lat: Double, lon: Double, country: Boolean = false): String {
    val localeId = Locale("id", "ID")
    var result = ""
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val geocoder = Geocoder(this, localeId)
        val geocodeListener = object : Geocoder.GeocodeListener {
            override fun onGeocode(addresses: MutableList<Address>) {
                result = if (country) addresses[0].countryName else addresses[0].locality
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

            if (country) addresses[0].countryName else addresses[0].locality
        } catch (e: Exception) {
            ""
        }
    }
    return result
}

fun String.countryCodeToFlagUrl(): String {
    return BASE_URL_FLAG.plus(this.lowercase()).plus(EXTENTION_SVG)
}

fun descriptionLocation(
    value: List<String>
): String {
    return value.joinToString()
}

@ColorInt
fun Context.resolveColorAttr(@AttrRes colorAttr: Int): Int {
    val resolvedAttr = resolveThemeAttr(colorAttr)
    val colorRes = if (resolvedAttr.resourceId != 0) resolvedAttr.resourceId else resolvedAttr.data
    return ContextCompat.getColor(this, colorRes)
}


private fun Context.resolveThemeAttr(@AttrRes attrRes: Int): TypedValue {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrRes, typedValue, true)
    return typedValue
}