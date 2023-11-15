package com.nishant.feature.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun <T> LoadingContent(state: com.nishant.core.repo.LoadingState<T>?, content: @Composable (t: T) -> Unit) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {

        when (state) {
            is com.nishant.core.repo.LoadingState.Loading -> {
                CircularProgressIndicator()
            }
            is com.nishant.core.repo.LoadingState.Success -> {
                content(state.t)
            }
            is com.nishant.core.repo.LoadingState.Error -> {
                Text(text = (state).e)
            }
            else -> Unit
        }
    }
}


