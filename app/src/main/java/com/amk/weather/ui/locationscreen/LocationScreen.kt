package com.amk.weather.ui.locationscreen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.amk.weather.R
import com.amk.weather.di.myModules
import com.amk.weather.ui.theme.Background
import com.amk.weather.ui.theme.WeatherTheme
import com.amk.weather.util.GPSUtils
import com.amk.weather.util.MyScreens
import com.amk.weather.util.appendTextDialog
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.maxkeppeker.sheets.core.CoreDialog
import com.maxkeppeker.sheets.core.models.CoreSelection
import com.maxkeppeker.sheets.core.models.base.*
import dev.burnoo.cokoin.Koin
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel

class LocationScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Koin(appDeclaration = { modules(myModules) }) {
                WeatherTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        LocationPage()
                    }
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun LocationPage() {
    val uiController = rememberSystemUiController()
    SideEffect { uiController.setStatusBarColor(Background) }
    val navigation = getNavController()
    val context = LocalContext.current
    val locationPermissionDialogState = rememberUseCaseState()
    val locationEnabledDialogState = rememberUseCaseState()
    val lat = mutableStateOf(0.0)
    val long = mutableStateOf(0.0)
    val gps = GPSUtils(context)

    val viewModel = getNavViewModel<LocationScreenViewModel>()

    if (!checkPermission(context)) {

        CoreDialog(
            state = locationPermissionDialogState,
            selection = CoreSelection(
                withButtonView = true,
                negativeButton = SelectionButton(
                    annotatedString = appendTextDialog("نه"),
                    IconSource(Icons.Rounded.Close),
                    ButtonStyle.ELEVATED
                ),
                positiveButton = SelectionButton(
                    annotatedString = appendTextDialog("آره حتما"),
                    IconSource(Icons.Rounded.LocationOn),
                    ButtonStyle.FILLED
                ),
                onPositiveClick = { requestPermission(context) },
                onNegativeClick = {
                    Toast.makeText(context, "دسترسی داده نشد!", Toast.LENGTH_SHORT).show()
                }
            ),
            header = Header.Custom {
                Text(
                    text = "مجوز دسترسی",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.nahid)),
                    fontWeight = FontWeight(800),
                    fontSize = 22.sp
                )
            },
            onPositiveValid = true,
            body = {
                Text(
                    text = "برای جستجوی خودکار شهر شما نیاز به مجوز موقعیت مکانی شما داریم، اجازه میدی؟",
                    textAlign = TextAlign.End,
                    fontFamily = FontFamily(Font(R.font.nahid)),
                    fontSize = 16.sp
                )
            },
        )
        locationPermissionDialogState.show()
    }

    if (!isLocationEnabled(context)) {
        CoreDialog(
            state = locationEnabledDialogState,
            selection = CoreSelection(
                withButtonView = true,
                negativeButton = SelectionButton(
                    annotatedString = appendTextDialog("نه"),
                    IconSource(Icons.Rounded.Close),
                    ButtonStyle.ELEVATED
                ),
                positiveButton = SelectionButton(
                    annotatedString = appendTextDialog("باشه"),
                    IconSource(Icons.Rounded.LocationOn),
                    ButtonStyle.FILLED
                ),
                onPositiveClick = {
                    gps.turnOnGPS()
                },
                onNegativeClick = {
                    Toast.makeText(context, "موقعیت مکانی گوشیت خاموشه!", Toast.LENGTH_SHORT).show()
                }
            ),
            header = Header.Custom {
                Text(
                    text = "مکان‌یابی خودکار",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.nahid)),
                    fontWeight = FontWeight(800),
                    fontSize = 22.sp
                )
            },
            onPositiveValid = true,
            body = {
                Text(
                    text = "برای موقعیت‌یابی خودکار نیازه که لوکیشن گوشیت روشن باشه، میخوای روشنش کنی؟",
                    textAlign = TextAlign.End,
                    fontFamily = FontFamily(Font(R.font.nahid)),
                    fontSize = 16.sp
                )
            },
        )
        locationEnabledDialogState.show()
    }

    Image(
        painter = painterResource(R.drawable.img_background),
        contentDescription = "Background",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MainAnimation()

        Text(
            text = "هیچ مکانی رو اضافه نکردی",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.nahid))
        )


        Button(
            onClick = {

                val fusedLocationProviderClient: FusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(context)

                if (checkPermission(context)) {
                    if (isLocationEnabled(context)) {

                        fusedLocationProviderClient.lastLocation.addOnCompleteListener(context as Activity) {
                            val location: Location? = it.result
                            if (location != null) {
                                lat.value = location.latitude
                                long.value = location.longitude

                                viewModel.saveData(context, location.latitude, location.longitude)

                                Toast.makeText(
                                    context,
                                    location.latitude.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                                Toast.makeText(
                                    context,
                                    location.longitude.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        navigation.navigate(MyScreens.MainScreen.route)

                    } else {
                        //Setting Open here
                        Toast.makeText(context, "لوکیشن گوشیت خاموشه!", Toast.LENGTH_SHORT)
                            .show()
                        gps.turnOnGPS()
                    }
                } else {
                    //request permission
                    requestPermission(context)
                }

            }
        ) {
            Text(
                text = "مکان‌یابی خودکار",
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.nahid))
            )
        }
    }

}

@Composable
fun MainAnimation() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.three)
    )

    LottieAnimation(
        modifier = Modifier
            .size(270.dp)
            .padding(top = 36.dp, bottom = 16.dp),
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}

fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER
    )
}

fun requestPermission(context: Context) {
    ActivityCompat.requestPermissions(
        context as Activity, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 100
    )
}

fun checkPermission(context: Context): Boolean {
    if (ActivityCompat.checkSelfPermission(
            context, android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        return true
    }
    Toast.makeText(context, "برای مکان‌یابی خودکار نیاز به دسترسی داریم.", Toast.LENGTH_SHORT)
        .show()
    return false
}

@Preview(showBackground = true)
@Composable
fun LocationScreenPreview() {
    WeatherTheme {
        LocationPage()
    }
}