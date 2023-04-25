package com.amk.weather.ui.daysweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amk.weather.R
import com.amk.weather.ui.theme.*
import com.amk.weather.util.WeekListDataInfo
import com.amk.weather.util.WeekWeatherData

class WeatherByDay : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DaysWeather()
                }
            }
        }
    }
}

@Composable
fun DaysWeather() {
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
//                .verticalScroll(rememberScrollState())
        ) {

            WeatherToolbar()

            TomorrowWeather()

            WeekWeather()

        }
    }

}

@Composable
fun WeatherToolbar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 18.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
            )
        }

        Text(
            text = "Next 7 Days", fontSize = 20.sp, fontFamily = interRegular
        )

        IconButton(onClick = { /*TODO*/ }) {

        }
    }
}

@Composable
fun TomorrowWeather() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(236.dp)
            .padding(top = 12.dp, start = 32.dp, end = 32.dp, bottom = 20.dp),
        shape = RoundedCornerShape(24.dp),
        color = BigCardViewBackground,
        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.70f))
    ) {

        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {

                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Tomorrow",
                        fontFamily = interSemiBold,
                        color = Color(48, 51, 69),
                        fontSize = 14.sp
                    )

                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier.size(66.dp)) {

                    }

                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "22 °",
                        fontSize = 14.sp,
                        fontFamily = interBold,
                        color = Color(48, 51, 69)
                    )

                    Image(
                        modifier = Modifier
                            .size(66.dp),
                        painter = painterResource(id = R.drawable.ic_sunny_cloudy),
                        contentDescription = null
                    )

                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                TomorrowWeatherItem(R.drawable.ic_umberella, "1 cm")
                TomorrowWeatherItem(R.drawable.ic_wind, "15 km/h")
                TomorrowWeatherItem(R.drawable.ic_humidity, "50 %")

            }
        }
    }
}

@Composable
fun TomorrowWeatherItem(itemImage: Int, itemValue: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            modifier = Modifier.size(44.dp),
            painter = painterResource(id = itemImage),
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = itemValue,
            fontFamily = interSemiBold,
            fontSize = 14.sp,
            color = Color(48, 51, 69)
        )
    }
}

@Composable
fun WeekWeather() {
    LazyColumn(
        modifier = Modifier.padding(top = 2.dp, bottom = 10.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
        userScrollEnabled = true
    ) {
        items(WeekListDataInfo.size) {
            WeekWeatherItem(WeekListDataInfo[it])
        }
    }
}

@Composable
fun WeekWeatherItem(weekDataList: WeekWeatherData) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .padding(top = 2.dp, start = 16.dp, end = 16.dp, bottom = 12.dp),
        shape = RoundedCornerShape(24.dp),
        color = CardViewBackground,
        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.40f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {

            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = weekDataList.day,
                    fontFamily = interSemiBold,
                    color = Color(48, 51, 69),
                    fontSize = 14.sp
                )

                IconButton(onClick = { /*TODO*/ }, modifier = Modifier.size(64.dp)) {

                }

            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = weekDataList.temp,
                    fontSize = 14.sp,
                    fontFamily = interBold,
                    color = Color(48, 51, 69)
                )

                Image(
                    modifier = Modifier
                        .size(64.dp),
                    painter = painterResource(id = weekDataList.img),
                    contentDescription = null
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherByDayPreview() {
    WeatherTheme {
        DaysWeather()
    }
}