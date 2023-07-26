package com.amk.weather.ui.locationscreen

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amk.weather.model.repository.daysWeather.DaysWeatherRepository
import com.amk.weather.model.repository.locationDataStore.LocationDataStore
import com.amk.weather.util.DWREx
import com.amk.weather.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class LocationScreenViewModel(
    private val daysWeatherRepository: DaysWeatherRepository,
) : ViewModel() {

    fun saveData(context: Context, lat: Double, long: Double) {
        val locData = LocationDataStore(context)
        viewModelScope.launch(coroutineExceptionHandler) {
            locData.saveData(lat, long)
        }
    }

}