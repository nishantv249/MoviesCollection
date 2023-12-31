package com.nishant.feature.ui.screens.bottom.popular

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.nishant.feature.ui.component.TopBar
import com.nishant.feature.ui.component.drawer.Drawer
import com.nishant.feature.ui.screens.BottomNavigationUI
import com.nishant.feature.ui.screens.MovieItem
import com.nishant.feature.ui.screens.MoviesVerticalGrid
import kotlinx.coroutines.launch

@Composable
fun PopularRoute(navHostController: NavHostController, onSearch : () -> Unit,onMovieClicked: (id: Int) -> Unit){
    
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
        }},
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
            Popular(genreId = genreId, onMovieClicked)
        }
    }
}

@Composable
fun Popular(genreId:String,onMovieClicked :(id :Int)->Unit,viewModel: PopularViewModel = hiltViewModel()) {

    LaunchedEffect(key1 = Unit) {
        viewModel.onNewGenreChange(genreId)
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val moviesList = remember(viewModel.pagingFLow, lifecycle) {
        viewModel.pagingFLow.flowWithLifecycle(lifecycle)
    }.collectAsLazyPagingItems()

    MoviesVerticalGrid(moviesList = moviesList,onMovieClicked)
}