package com.amk.weather.model.repository.locationDataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.amk.weather.model.data.LocationData
import com.amk.weather.util.LAT
import com.amk.weather.util.LONG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class LocationDataStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storeData")
    }

    suspend fun saveData(first: Double, second: Double) {
        context.dataStore.edit { preferences ->
            preferences[LAT] = first
            preferences[LONG] = second
        }
    }

    val getData: Flow<LocationData> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val first = preferences[LAT] ?: 0.0
            val second = preferences[LONG] ?: 0.0
            LocationData(first, second)
        }

    fun isKeyStored(key: Preferences.Key<Double>): Flow<Boolean> =
        context.dataStore.data.map { preferences ->
            preferences.contains(key)
        }

}