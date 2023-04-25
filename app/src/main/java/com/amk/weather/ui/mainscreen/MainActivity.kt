package com.amk.weather.ui.mainscreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amk.weather.R
import com.amk.weather.ui.daysweather.WeatherByDay
import com.amk.weather.ui.theme.*
import com.amk.weather.util.TempDataInfo
import com.amk.weather.util.TemperatureData

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainWeatherScreen()
                }
            }
        }
    }
}

@Composable
fun MainWeatherScreen() {
    val context = LocalContext.current

    Image(
        painter = painterResource(R.drawable.img_background),
        contentDescription = "Background",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
    )

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            MainToolbar()

            CityName()

            Weather()

            WeatherInfo()

            TempDays() {
                context.startActivity(Intent(context, WeatherByDay::class.java))
            }

            Divider(
                color = Color(226, 162, 114),
                thickness = 0.5.dp,
                modifier = Modifier.padding(start = 32.dp, top = 8.dp, bottom = 8.dp, end = 32.dp)
            )

            Temperature()

        }
    }
}

@Composable
fun MainToolbar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = { /*TODO*/ }) {
            Image(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(40.dp)
            )
        }

        Text(text = "Weather App", fontSize = 22.sp, fontFamily = interMedium)

        IconButton(onClick = { /*TODO*/ }) {
            Image(
                painter = painterResource(id = R.drawable.ic_menu),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(40.dp)
            )
        }
    }

}

@Composable
fun CityName() {
    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    )
    {

        Text(
            modifier = Modifier.padding(top = 8.dp, start = 32.dp),
            text = "Stockholm,\n" +
                    "Sweden",
            fontFamily = interMedium,
            fontSize = 34.sp,
            color = Color(49, 51, 65)
        )

        Text(
            text = "Tue, Jun 30",
            modifier = Modifier.padding(top = 4.dp, start = 32.dp),
            color = Color(154, 147, 140),
            fontFamily = interRegular
        )

    }
}

@Composable
fun Weather() {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 6.dp, end = 8.dp)
    ) {

        Image(
            modifier = Modifier
                .size(193.dp, 190.dp),
            painter = painterResource(id = R.drawable.ic_rainy), contentDescription = null
        )

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "19",
                fontFamily = interBold,
                fontSize = 80.sp,
                color = Color(48, 51, 69)
            )

            Text(
                modifier = Modifier.padding(bottom = 18.dp),
                text = "Rainy",
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
fun WeatherInfo() {
    WeatherInfoItem(R.drawable.ic_umberella, "RainFall", "3cm")
    WeatherInfoItem(R.drawable.ic_wind, "Wind", "19km/h")
    WeatherInfoItem(R.drawable.ic_humidity, "Humidity", "64%")
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
                modifier = Modifier
                    .size(46.dp),
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
            modifier = Modifier
                .padding(end = 38.dp),
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
fun Temperature() {
    LazyRow(
        modifier = Modifier.padding(top = 8.dp, bottom = 14.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
        userScrollEnabled = true
    ) {
        items(TempDataInfo.size) {
            TemperatureItem(
                TempDataInfo[it], TempDataInfo[it].time == "now"
            )
        }
    }
}

@Composable
fun TemperatureItem(temperatureData: TemperatureData, isNowItem: Boolean) {
    Surface(
        modifier = Modifier
            .padding(start = 6.dp, end = 6.dp)
            .size(56.dp, 98.dp),
        shape = RoundedCornerShape(48.dp),
        color = if (isNowItem) {
            TemperatureItemSelectedBackground
        } else {
            TemperatureItemBackground
        }
    ) {

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = temperatureData.time,
                fontSize = 14.sp,
                fontFamily = interRegular,
                color = if (isNowItem) {
                    Color(48, 51, 69)
                } else {
                    TemperatureItemTime
                },
            )

            Image(
                modifier = Modifier.size(34.dp),
                painter = painterResource(temperatureData.img),
                contentDescription = null
            )

            Text(
                text = temperatureData.temp,
                fontSize = 14.sp,
                fontFamily = interBold,
                color = Color(48, 51, 69),
            )

        }

    }

}

@Composable
fun TempDays(onDaysWeather: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(start = 32.dp, top = 14.dp)
        ) {

            Text(
                text = "Today",
                fontSize = 14.sp,
                fontFamily = interBold,
                color = Color(49, 51, 65),
            )

            Text(
                modifier = Modifier.padding(start = 24.dp),
                text = "Tomorrow",
                fontSize = 14.sp,
                fontFamily = interRegular,
                color = Color(214, 153, 107),
            )

        }


        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(top = 14.dp, end = 20.dp)
        ) {

            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                text = "Next 7 Days",
                fontSize = 14.sp,
                fontFamily = interRegular,
                color = Color(214, 153, 107),
            )

            IconButton(onClick = { onDaysWeather.invoke() }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                )
            }

        }


    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherTheme {
        MainWeatherScreen()
    }
}