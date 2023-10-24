package com.nishant.moviescollection.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nishant.moviescollection.network.Result

@Composable
fun <T> LoadingContent(state: Result<T>?, content: @Composable (t: T) -> Unit) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {

        when (state) {
            is com.nishant.moviescollection.network.Result.Loading -> {
                CircularProgressIndicator()
            }
            is com.nishant.moviescollection.network.Result.Success -> {
                content(state.t)
            }
            is Result.Error -> {
                Text(text = (state as com.nishant.moviescollection.network.Result.Error).e)
            }
            else -> Unit
        }
    }
}

