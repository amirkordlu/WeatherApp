package com.amk.weather.util

import androidx.datastore.preferences.core.doublePreferencesKey

const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

const val APP_ID = "7f2e7158c131ac45e426e00197a2995b"

val LAT = doublePreferencesKey("latitude")
val LONG = doublePreferencesKey("longitude")