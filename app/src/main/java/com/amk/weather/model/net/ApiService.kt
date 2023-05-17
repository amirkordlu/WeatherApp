package com.amk.weather.model.net

import com.amk.weather.model.data.CurrentWeatherResponse
import com.amk.weather.model.data.DaysWeatherResponse
import com.amk.weather.util.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double = 35.6589015,
        @Query("lon") lon: Double = 51.0586022,
        @Query("appid") appid: String = "0fc44a70fe8abd7f3ba7b06ee76148be"
    ): CurrentWeatherResponse


    @GET("forecast/daily")
    suspend fun getDaysWeather(
        @Query("lat") lat: Double = 35.6589015,
        @Query("lon") lon: Double = 51.0586022,
        @Query("appid") appid: String = "0fc44a70fe8abd7f3ba7b06ee76148be"
    ): DaysWeatherResponse

}

fun createApiService(): ApiService {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(ApiService::class.java)
}