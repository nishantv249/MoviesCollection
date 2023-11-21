package com.nishant.feature.ui.screens.bottom.now

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.nishant.core.network.models.MovieItemDto
import com.nishant.feature.ui.screens.MovieItem
import com.nishant.feature.ui.screens.MoviesVerticalGrid

@Composable
fun NowPlaying(genreId : String,onMovieClicked : (id : Int) -> Unit,nowPlayingViewModel: NowPlayingViewModel = hiltViewModel(),){
    LaunchedEffect(key1 = genreId) {
        nowPlayingViewModel.onNewGenre(genreId)
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val moviesList = remember(lifecycle,nowPlayingViewModel.nowPlayingPagingFlow) {
        nowPlayingViewModel.nowPlayingPagingFlow.flowWithLifecycle(lifecycle)
    }.collectAsLazyPagingItems()

    MoviesVerticalGrid(moviesList,onMovieClicked)
}
