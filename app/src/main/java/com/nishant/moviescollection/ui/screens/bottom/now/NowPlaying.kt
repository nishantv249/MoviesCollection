package com.nishant.moviescollection.ui.screens.bottom.now

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.nishant.moviescollection.ui.screens.MovieItem

@Composable
fun NowPlaying(genreId : String,onMovieClicked : (id : Int) -> Unit,nowPlayingViewModel: NowPlayingViewModel = hiltViewModel(),){
    LaunchedEffect(key1 = genreId) {
        nowPlayingViewModel.onNewGenre(genreId)
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val moviesList = remember(lifecycle,nowPlayingViewModel.nowPlayingPagingFlow) {
        nowPlayingViewModel.nowPlayingPagingFlow.flowWithLifecycle(lifecycle)
    }.collectAsLazyPagingItems()

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
                item (span = { GridItemSpan(2)}){
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

}