package com.amk.weather.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amk.weather.di.myModules
import com.amk.weather.model.repository.locationDataStore.LocationDataStore
import com.amk.weather.ui.daysweather.DaysWeather
import com.amk.weather.ui.locationscreen.LocationPage
import com.amk.weather.ui.mainscreen.MainWeatherScreen
import com.amk.weather.ui.theme.WeatherTheme
import com.amk.weather.util.LAT
import com.amk.weather.util.LONG
import com.amk.weather.util.MyScreens
import dev.burnoo.cokoin.Koin
import dev.burnoo.cokoin.navigation.KoinNavHost
import kotlinx.coroutines.flow.Flow
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Koin(appDeclaration = {
                androidContext(this@MainActivity)
                modules(myModules)
            }) {
                WeatherTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        WeatherUI()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherTheme {
        WeatherUI()
    }
}

@Composable
fun WeatherUI() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val locData = LocationDataStore(context)
    val isLat: Flow<Boolean> = locData.isKeyStored(LAT)
    val isLong: Flow<Boolean> = locData.isKeyStored(LONG)
    val isLatStored: Boolean = isLat.collectAsState(initial = true).value
    val isLongStored: Boolean = isLong.collectAsState(initial = true).value

    LaunchedEffect(key1 = Pair(isLat, isLong)) {
        if (isLatStored && isLongStored) {
            navController.navigate(MyScreens.MainScreen.route)
        } else {
            navController.navigate(MyScreens.LocationScreen.route)
        }
    }

    KoinNavHost(
        navController = navController,
        startDestination = MyScreens.MainScreen.route
    ) {

        composable(MyScreens.MainScreen.route) {
            MainWeatherScreen()

        }

        composable(MyScreens.LocationScreen.route) {
            LocationPage()
        }

        composable(
            route = MyScreens.WeatherScreen.route
        ) {
            DaysWeather()
        }

    }
}