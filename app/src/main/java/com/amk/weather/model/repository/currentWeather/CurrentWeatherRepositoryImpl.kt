package com.amk.weather.model.repository.currentWeather

import com.amk.weather.model.data.CurrentWeatherResponse
import com.amk.weather.model.net.ApiService

class CurrentWeatherRepositoryImpl(
    private val apiService: ApiService
) : CurrentWeatherRepository {

    override suspend fun getCurrentWeather(): CurrentWeatherResponse {
        return apiService.getCurrentWeather()
    }

}