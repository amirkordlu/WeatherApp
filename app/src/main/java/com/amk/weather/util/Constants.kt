package com.amk.weather.util

import com.amk.weather.R

val WeekListDataInfo = listOf(
    WeekWeatherData("Thursday", "21 °", R.drawable.ic_sun),
    WeekWeatherData("Friday", "24 °", R.drawable.ic_sun),
    WeekWeatherData("Saturday", "18 °", R.drawable.ic_sunny_cloudy),
    WeekWeatherData("Sunday", "12 °", R.drawable.ic_cloudy),
    WeekWeatherData("Monday", "16 °", R.drawable.ic_cloudy_rainy),
    WeekWeatherData("Tuesday", "18 °", R.drawable.ic_cloudy_rainy),
)

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