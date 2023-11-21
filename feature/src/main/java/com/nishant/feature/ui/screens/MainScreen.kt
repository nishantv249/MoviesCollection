package com.nishant.feature.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid

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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.nishant.core.network.api.MoviesApiService
import com.nishant.core.network.models.MovieItemDto
import com.nishant.feature.ui.component.TopBar
import com.nishant.feature.ui.component.drawer.Drawer
import com.nishant.feature.ui.currentRoute
import com.nishant.feature.ui.navigation.Screen
import com.nishant.feature.ui.screens.bottom.now.NowPlaying
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
            NowPlaying(genreId = genreId, onMovieClicked)
        }
    }

}

@Composable
fun BottomNavigationUI(navHostController: NavHostController, genreId: String) {
    BottomNavigation {
        val list = listOf(Screen.Home, Screen.Popular,Screen.TopRated,Screen.Upcoming)
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
fun MoviesVerticalGrid(
    moviesList: LazyPagingItems<MovieItemDto>,
    onMovieClicked: (id: Int) -> Unit
) {
    if(moviesList.loadState.refresh is LoadState.Loading){
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }else{
        LazyVerticalGrid(columns = GridCells.Fixed(2) ,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(8.dp)) {

            items(moviesList.itemCount) {
                moviesList[it]?.let { it1 -> MovieItem(movieItem = it1, onMovieClicked) }
            }
            if(moviesList.loadState.append is LoadState.Loading){
                item (span = { GridItemSpan(2) }){
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}


@Composable
fun MovieItem(movieItem: MovieItemDto, onMovieClicked: (id: Int) -> Unit) {

    Column (modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()){
        val painter =
            rememberAsyncImagePainter(model = MoviesApiService.IMAGE_URL.plus(movieItem.backdropPath))
        Image(
            painter = painter, contentDescription = "",
            Modifier
                .fillMaxWidth()
                .height(240.dp)
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
