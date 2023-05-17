package com.amk.weather.model.repository.currentWeather

import com.amk.weather.model.data.CurrentWeatherResponse
import com.amk.weather.model.data.Main

interface CurrentWeatherRepository {

    suspend fun getCurrentWeather(): CurrentWeatherResponse

}