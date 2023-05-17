package com.amk.weather.util

import com.amk.weather.R

const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

val TempDataInfo = listOf(
    TemperatureData("11:00", R.drawable.ic_cloudy, "20 °"),
    TemperatureData("now", R.drawable.ic_sunny_cloudy, "19 °"),
    TemperatureData("13:00", R.drawable.ic_sun, "21 °"),
    TemperatureData("14:00", R.drawable.ic_sun, "20 °"),
    TemperatureData("15:00", R.drawable.ic_cloudy, "20 °"),
    TemperatureData("16:00", R.drawable.ic_cloudy, "19 °"),
    TemperatureData("17:00", R.drawable.ic_rainy, "14 °"),
    TemperatureData("18:00", R.drawable.ic_cloudy_rainy, "16 °"),
    TemperatureData("19:00", R.drawable.ic_cloudy_rainy, "17 °"),
    TemperatureData("20:00", R.drawable.ic_cloudy, "13 °"),
)