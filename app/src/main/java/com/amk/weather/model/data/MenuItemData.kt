package com.amk.weather.model.data

import androidx.compose.ui.graphics.painter.Painter

data class NavigationDrawerItem(
    val image: Painter, val label: String, val showUnreadBubble: Boolean = false
)
