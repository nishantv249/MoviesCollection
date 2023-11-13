package com.nishant.moviescollection.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.nishant.moviescollection.R
import com.nishant.moviescollection.il.painter.rememberAsyncILPainter
import com.nishant.moviescollection.ui.currentRoute
import com.nishant.moviescollection.network.ApiURL
import com.nishant.moviescollection.network.models.MovieItem
import com.nishant.moviescollection.ui.component.drawer.Drawer
import com.nishant.moviescollection.ui.component.TopBar
import com.nishant.moviescollection.ui.navigation.Screen
import com.nishant.moviescollection.ui.screens.bottom.now.NowPlaying
import kotlinx.coroutines.launch

@Composable
fun MainScreen(navHostController: NavHostController,
    onMovieClicked: (id: Int) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    var genreId by remember {
        mutableStateOf("")
    }

    Scaffold(scaffoldState = scaffoldState,
        drawerGesturesEnabled = (currentRoute(navController = navHostController) == Screen.Home.route
                || currentRoute(navController = navHostController) == Screen.Popular.route),
        topBar = {
            TopBar({
                coroutineScope.launch {
                    scaffoldState.drawerState.open()
                }
            }, {
                navHostController.navigate("search")
            })
        },
        drawerContent = {
            Drawer {
                genreId = it
                coroutineScope.launch {
                    scaffoldState.drawerState.close()
                }
            }
        },
        bottomBar = {BottomNavigationUI(navHostController, genreId)}
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it), contentAlignment = Alignment.Center
        ) {
            NowPlaying(genreId = "", onMovieClicked)
        }
    }

}

@Composable
fun BottomNavigationUI(navHostController: NavHostController, genreId: String) {
    BottomNavigation {
        val list = listOf(Screen.Home, Screen.Popular)
        var selectedItem by remember {
            mutableStateOf(Screen.Home.route)
        }


        list.forEach { item ->
            item.icon?.let {
                BottomNavigationItem(
                    selected = item.route == currentRoute(navController = navHostController),
                    onClick =
                    {
                        selectedItem = item.route
                        navHostController.navigate(selectedItem) {
                            navHostController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    unselectedContentColor = Color.White.copy(0.40f),
                    icon = it,
                    label = { Text(item.title) },
                    selectedContentColor = Color.White
                )
            }

        }

    }

}

@Composable
fun MovieItem(movieItem: MovieItem, onMovieClicked: (id: Int) -> Unit) {

    Column (modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()){
        val painter =
            rememberAsyncILPainter(url = ApiURL.IMAGE_URL.plus(movieItem.backdropPath))
        Image(
            painter = painter, contentDescription = "",
            Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clickable {
                    onMovieClicked(movieItem.id)
                }
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier
            .height(8.dp))
        Text(text = movieItem.title, maxLines = 1)
    }

}
