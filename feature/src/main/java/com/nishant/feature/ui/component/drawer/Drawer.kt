package com.nishant.feature.ui.component.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.nishant.core.domain.models.Genre
import com.nishant.core.repo.LoadingState

@Composable
fun Drawer(viewModel: DrawerViewModel = hiltViewModel(), onGenreSelected : (genreId : String) -> Unit) {

    LaunchedEffect(key1 = 0) {
        viewModel.getGenres()
    }
    val state = viewModel.genresState
    Box(contentAlignment = Alignment.Center) {

        if(state.value is LoadingState.Loading){
            CircularProgressIndicator()
        }else if(state.value is LoadingState.Success){
            val genres = (state.value as LoadingState.Success<List<Genre>>).t
            LazyColumn{
                items(genres){
                    Text(text = it.name , Modifier.clickable {
                        onGenreSelected(it.id.toString())
                    })
                }
            }
        }
    }


}