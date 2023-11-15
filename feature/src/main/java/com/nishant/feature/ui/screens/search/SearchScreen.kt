package com.nishant.feature.ui.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nishant.core.network.models.MoviesDto
import com.nishant.core.repo.LoadingState
import com.nishant.feature.ui.component.SearchBar
import com.nishant.feature.ui.screens.MovieItem

@Composable
fun SearchScreen(searchViewModel: SearchViewModel = hiltViewModel(),
                 onMovieClicked : (id : Int) -> Unit, onBackPressed : () -> Unit) {

    val searchResult = searchViewModel.flow.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(onBackPressed) {
            searchViewModel.search(it)
        }
        SearchResult(searchResult,onMovieClicked)
    }
}

@Composable
fun SearchResult(searchResult: State<LoadingState<MoviesDto>>, onMovieClicked: (id: Int) -> Unit) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()){
        when (searchResult.value) {
            is LoadingState.Empty -> {
                Text(text = "Please type to search")
            }
            is LoadingState.Loading -> {
                CircularProgressIndicator()
            }
            is LoadingState.Success -> {
                val list = (searchResult.value as LoadingState.Success<MoviesDto>).t
                LazyVerticalGrid(columns = GridCells.Fixed(2) , verticalArrangement =
                Arrangement.spacedBy(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)){
                    items(list.results.size){ it1->
                        MovieItem(movieItem = list.results[it1]) { id ->
                            onMovieClicked(id)
                        }
                    }
                }
            }
            else -> Unit
        }
    }
}
