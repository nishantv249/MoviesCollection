package com.nishant.feature.ui.navigation

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable

sealed class Screen(
    val route: String,
    val title: String = "Home",
    val icon: @Composable() (() -> Unit)?,
    val objectName: String = "",
    val objectPath: String = ""
){
    object Home : Screen("Home", icon = { Icon(Icons.Filled.Home, contentDescription = "")}, objectName = "genreId")
    object Popular : Screen("popular","Popular",
        {Icon(Icons.Filled.Face, contentDescription = "")}, objectName = "genreID")
    object TopRated : Screen("top_rated","Top Rated",{Icon(Icons.Filled.Star,contentDescription = "")})
    object Upcoming : Screen("upcoming","Upcoming",{ Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "")})

    object MovieDetail : Screen("detail","MovieDetail",null,"id")

    object ArtistDetail : Screen("artist","ArtistDetail",null,"id")

}