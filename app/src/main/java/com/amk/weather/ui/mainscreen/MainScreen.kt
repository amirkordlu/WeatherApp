package com.amk.weather.ui.mainscreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amk.weather.R
import com.amk.weather.di.myModules
import com.amk.weather.model.data.CurrentWeatherResponse
import com.amk.weather.model.data.HourlyWeather
import com.amk.weather.model.data.HourlyWeatherResponse
import com.amk.weather.model.data.LocationData
import com.amk.weather.model.data.NavigationDrawerItem
import com.amk.weather.model.repository.locationDataStore.LocationDataStore
import com.amk.weather.ui.shimmer.MainScreenShimmer
import com.amk.weather.ui.shimmer.TemperatureShimmer
import com.amk.weather.ui.theme.*
import com.amk.weather.util.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.burnoo.cokoin.Koin
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Koin(appDeclaration = { modules(myModules) }) {
                WeatherTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                    ) {
                        MainWeatherScreen()
                    }
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun MainWeatherScreen() {
    val context = LocalContext.current
    val uiController = rememberSystemUiController()
    SideEffect { uiController.setStatusBarColor(Background) }
    val navigation = getNavController()
    val viewModel = getNavViewModel<MainScreenViewModel>()
    val hourlyViewModel = getNavViewModel<HourlyWeatherViewModel>()
    val locData = LocationDataStore(context)
    val locationData = locData.getData.collectAsState(
        initial = LocationData(
            0.0, 0.0
        )
    )
    val lat = mutableStateOf(locationData.value.lat)
    val long = mutableStateOf(locationData.value.long)
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    if (lat.value != 0.0 && long.value != 0.0) {
        viewModel.getWeatherInfo(lat.value, long.value)
        hourlyViewModel.getHourlyWeather(lat.value, long.value)
    }


    if (!NetworkChecker(context).isInternetConnected) {
        NoInternetDialog(onTryAgain = {
            if (NetworkChecker(context).isInternetConnected) {
                viewModel.getWeatherInfo(lat.value, long.value)
                hourlyViewModel.getHourlyWeather(lat.value, long.value)
            } else {
                Toast.makeText(context, "No internet connection!", Toast.LENGTH_SHORT).show()
            }
        }, onDismiss = {})
    }


    Scaffold(modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        drawerContent = {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                DrawerContent { itemLabel ->
                    Toast.makeText(context, itemLabel, Toast.LENGTH_SHORT).show()
                    coroutineScope.launch {
                        // delay for the ripple effect
                        delay(timeMillis = 250)
                        scaffoldState.drawerState.close()
                    }
                }
            }
        }) {

        it.calculateBottomPadding()

        Image(
            painter = painterResource(R.drawable.img_background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        if (viewModel.showLoading.value) {

            MainScreenShimmer()

        } else {

            Box(modifier = Modifier.fillMaxSize()) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {

                    MainToolbar(onSearchClicked = { navigation.navigate(MyScreens.LocationScreen.route) },
                        onDrawerClicked = {
                            coroutineScope.launch(coroutineExceptionHandler) {
                                scaffoldState.drawerState.open()
                            }
                        })

                    CityName(viewModel.weatherInfo.value, getDateOfMobile())

                    Weather(viewModel.weatherInfo.value)

                    WeatherInfo(viewModel.weatherInfo.value)

                    TempDays(onTodayClicked = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(
                                0
                            )
                        }
                    },
                        onTomorrowClicked = {
                            coroutineScope.launch {
                                listState.animateScrollToItem(
                                    8
                                )
                            }
                        },
                        onDaysWeather = { navigation.navigate(MyScreens.WeatherScreen.route) })

                    Divider(
                        color = Color(226, 162, 114),
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(
                            start = 32.dp, top = 8.dp, bottom = 8.dp, end = 32.dp
                        )
                    )

                    if (hourlyViewModel.hourlyWeather.value.cod == 200) {
                        Temperature(hourlyViewModel.hourlyWeather.value, listState)
                    } else {
                        LazyRow(
                            modifier = Modifier.padding(
                                start = 16.dp,
                                top = 2.dp,
                                bottom = 10.dp
                            )
                        ) {
                            items(8) {
                                TemperatureShimmer()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainToolbar(onSearchClicked: () -> Unit, onDrawerClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = { onDrawerClicked.invoke() }) {
            Image(
                painter = painterResource(id = R.drawable.ic_menu),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(40.dp)
            )
        }

        Text(text = "Eghlim App", fontSize = 22.sp, fontFamily = interMedium)

        IconButton(onClick = { onSearchClicked.invoke() }) {
            Image(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(40.dp)
            )
        }
    }

}

@Composable
fun CityName(weatherName: CurrentWeatherResponse, date: String) {
    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            modifier = Modifier.padding(top = 8.dp, start = 32.dp),
            text = weatherName.name,
            fontFamily = interMedium,
            fontSize = 34.sp,
            color = Color(49, 51, 65)
        )

        Text(
            text = date,
            modifier = Modifier.padding(top = 4.dp, start = 32.dp),
            color = Color(154, 147, 140),
            fontFamily = interRegular
        )

    }
}

@Composable
fun Weather(weather: CurrentWeatherResponse) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 6.dp, end = 8.dp)
    ) {

        Image(
            modifier = Modifier.size(193.dp, 190.dp),
            painter = painterResource(id = weatherIcon(weather.weather[0].icon)),
            contentDescription = null
        )

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = convertKelvinToCelsius(weather.main.temp).toString(),
                fontFamily = interBold,
                fontSize = 80.sp,
                color = Color(48, 51, 69)
            )

            Text(
                modifier = Modifier.padding(bottom = 18.dp),
                text = weather.weather[0].main,
                fontFamily = interRegular,
                fontSize = 30.sp,
                color = Color(48, 51, 69)
            )
        }

        Text(
            modifier = Modifier
                .align(Alignment.Top)
                .padding(top = 22.dp),
            text = "° C",
            fontFamily = interLight,
            fontSize = 18.sp,
            color = Color(48, 51, 69)
        )
    }
}

@Composable
fun WeatherInfo(weatherInfo: CurrentWeatherResponse) {
    WeatherInfoItem(R.drawable.ic_pressure, "Pressure", "${weatherInfo.main.pressure} Pa")
    WeatherInfoItem(
        R.drawable.ic_wind,
        "Wind",
        "${convertMeterOnMinToKilometerOnHour(weatherInfo.wind.speed)} km/h"
    )
    WeatherInfoItem(R.drawable.ic_humidity, "Humidity", "${weatherInfo.main.humidity} %")
}

@Composable
fun WeatherInfoItem(itemImage: Int, itemText: String, itemValue: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .padding(start = 32.dp, end = 32.dp, bottom = 8.dp),
        shape = RoundedCornerShape(24.dp),
        color = CardViewBackground,
        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.70f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(46.dp),
                painter = painterResource(id = itemImage),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = itemText,
                fontFamily = interRegular,
                color = Color(48, 51, 69),
                fontSize = 14.sp
            )

        }


        Row(
            modifier = Modifier.padding(end = 38.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            Text(
                text = itemValue,
                fontSize = 14.sp,
                fontFamily = interRegular,
                color = Color(48, 51, 69),
            )

        }

    }
}

@Composable
fun Temperature(hourlyWeather: HourlyWeatherResponse, lazyRowState: LazyListState) {
    LazyRow(
        modifier = Modifier.padding(top = 8.dp, bottom = 14.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
        userScrollEnabled = true,
        state = lazyRowState
    ) {
        items(hourlyWeather.list.size) {
            if (it in 0..7) {
                TemperatureItem(
                    hourlyWeather.list[it]
                )
            }
        }
    }
}

@Composable
fun TemperatureItem(hourlyWeather: HourlyWeather) {
    Surface(
        modifier = Modifier
            .padding(start = 6.dp, end = 6.dp)
            .size(56.dp, 98.dp),
        shape = RoundedCornerShape(48.dp),
        color = TemperatureItemBackground
    ) {

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = formatTimeString(hourlyWeather.dt_txt),
                fontSize = 14.sp,
                fontFamily = interRegular,
                color = TemperatureItemTime,
            )

            Image(
                modifier = Modifier.size(34.dp),
                painter = painterResource(weatherIcon(hourlyWeather.weather[0].icon)),
                contentDescription = null
            )

            Text(
                text = convertKelvinToCelsius(hourlyWeather.main.temp).toString() + " °",
                fontSize = 14.sp,
                fontFamily = interBold,
                color = Color(48, 51, 69),
            )

        }

    }

}

@SuppressLint("UnrememberedMutableState")
@Composable
fun TempDays(
    onTodayClicked: () -> Unit, onTomorrowClicked: () -> Unit, onDaysWeather: () -> Unit
) {
    val todayClicked = remember { mutableStateOf(1) }
    val tomorrowClicked = remember { mutableStateOf(0) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(start = 32.dp, top = 14.dp)
        ) {

            ClickableText(text = buildAnnotatedString {
                append("Today")
            }, onClick = {
                onTodayClicked.invoke()
                todayClicked.value = 1
                tomorrowClicked.value = 0
            }, style = if (todayClicked.value == 1) {
                todayStyle
            } else {
                tomorrowStyle
            }
            )

            ClickableText(
                text = buildAnnotatedString {
                    append("Tomorrow")
                },
                onClick = {
                    onTomorrowClicked.invoke()
                    todayClicked.value = 0
                    tomorrowClicked.value = 1
                },
                modifier = Modifier.padding(start = 24.dp),
                style = if (tomorrowClicked.value == 0) {
                    tomorrowStyle
                } else {
                    todayStyle
                }
            )

        }


        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(top = 14.dp, end = 20.dp)
        ) {

            ClickableText(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontFamily = interRegular,
                            fontSize = 14.sp,
                            color = Color(214, 153, 107)
                        )
                    ) {
                        append("Next Days")
                    }
                },
                onClick = { onDaysWeather.invoke() },
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            IconButton(onClick = { onDaysWeather.invoke() }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }

        }


    }
}

@Composable
fun NoInternetDialog(
    onTryAgain: () -> Unit, onDismiss: () -> Unit
) {
    AlertDialog(onDismissRequest = { onDismiss.invoke() },
        title = { Text(text = "No Internet Connection") },
        text = { Text(text = "Please check your internet connection and try again.") },
        confirmButton = {
            TextButton(onClick = { onTryAgain.invoke() }) {
                Text(text = "Try again")
            }
        })
}

@Composable
private fun DrawerContent(
    gradientColors: List<Color> = listOf(Color(0xFFFFD291), Color(0xFFF39876)),
    itemClick: (String) -> Unit
) {

    val itemsList = prepareNavigationDrawerItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = gradientColors)),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 36.dp)
    ) {

        item {

            // user's image
            Image(
                modifier = Modifier
                    .size(size = 120.dp)
                    .clip(shape = CircleShape),
                painter = painterResource(id = R.drawable.ic_sun),
                contentDescription = "Profile Image"
            )

            // user's name
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = "Eghlim's User",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // user's email
            Text(
                modifier = Modifier.padding(top = 8.dp, bottom = 30.dp),
                text = "user@email.com",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.White
            )
        }

        items(itemsList) { item ->
            NavigationListItem(item = item) {
                itemClick(item.label)
            }
        }
    }
}

@Composable
private fun NavigationListItem(
    item: NavigationDrawerItem, unreadBubbleColor: Color = Color(0xFF0FFF93), itemClick: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            itemClick()
        }
        .padding(horizontal = 24.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End) {

        // label
        Text(
            modifier = Modifier.padding(end = 16.dp),
            text = item.label,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            fontFamily = nahidFont
        )

        // icon and unread bubble
        Box {

            Icon(
                modifier = Modifier
                    .padding(all = if (item.showUnreadBubble && item.label == "Messages") 5.dp else 2.dp)
                    .size(size = if (item.showUnreadBubble && item.label == "Messages") 24.dp else 28.dp),
                painter = item.image,
                contentDescription = null,
                tint = Color.White
            )

            // unread bubble
            if (item.showUnreadBubble) {
                Box(
                    modifier = Modifier
                        .size(size = 8.dp)
                        .align(alignment = Alignment.TopEnd)
                        .background(color = unreadBubbleColor, shape = CircleShape)
                )
            }
        }

    }
}

@Composable
private fun prepareNavigationDrawerItems(): List<NavigationDrawerItem> {
    val itemsList = arrayListOf<NavigationDrawerItem>()

    itemsList.add(
        NavigationDrawerItem(
            image = painterResource(id = R.drawable.ic_home), label = "خانه"
        )
    )
    itemsList.add(
        NavigationDrawerItem(
            image = painterResource(id = R.drawable.ic_bell),
            label = "پیام ها",
            showUnreadBubble = true
        )
    )
    itemsList.add(
        NavigationDrawerItem(
            image = painterResource(id = R.drawable.ic_credit_card), label = "اشتراک"
        )
    )
    itemsList.add(
        NavigationDrawerItem(
            image = painterResource(id = R.drawable.ic_settings), label = "تنظیمات"
        )
    )
    itemsList.add(
        NavigationDrawerItem(
            image = painterResource(id = R.drawable.ic_log_out), label = "خروج"
        )
    )

    return itemsList
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherTheme {
        MainWeatherScreen()
    }
}