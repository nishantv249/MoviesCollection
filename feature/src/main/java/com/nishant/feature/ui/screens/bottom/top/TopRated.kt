package com.nishant.feature.ui.screens.bottom.top

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.nishant.feature.ui.component.TopBar
import com.nishant.feature.ui.component.drawer.Drawer
import com.nishant.feature.ui.screens.BottomNavigationUI
import com.nishant.feature.ui.screens.MoviesVerticalGrid
import com.nishant.feature.ui.screens.bottom.popular.Popular
import kotlinx.coroutines.launch


@Composable
fun TopRatedRoute(navHostController: NavHostController, onSearch : () -> Unit, onMovieClicked: (id: Int) -> Unit){

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    var genreId by remember {
        mutableStateOf("")
    }

    Scaffold(scaffoldState = scaffoldState,
        topBar = { TopBar(openDrawer = {
            coroutineScope.launch {
                scaffoldState.drawerState.open()
            }
        }) {
            onSearch()
        }
        },
        bottomBar = {
            BottomNavigationUI(navHostController = navHostController , genreId = "" )
        },
        drawerContent = { Drawer( onGenreSelected = {
            genreId = it
            coroutineScope.launch {
                scaffoldState.drawerState.close()
            }
        }) }
    ) {
        Box(modifier = Modifier.padding(it)){
            TopRated(genreId = genreId, onMovieClicked)
        }
    }
}


@Composable
fun TopRated(genreId : String,onMoviesClicked : (id : Int) -> Unit,topRatedViewModel: TopRatedViewModel = hiltViewModel()) {

    LaunchedEffect(key1 = genreId) {
        topRatedViewModel.onNewGenre(genreId)
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val moviesList = remember(lifecycle,topRatedViewModel.topRatedMovieFlow) {
        topRatedViewModel.topRatedMovieFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
    }.collectAsLazyPagingItems()

    MoviesVerticalGrid(moviesList = moviesList,onMoviesClicked)

}