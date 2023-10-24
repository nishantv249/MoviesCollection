package com.nishant.moviescollection.ui.component.drawer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nishant.moviescollection.network.Result
import com.nishant.moviescollection.network.models.Genres
import com.nishant.moviescollection.repo.IMoviesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawerViewModel @Inject constructor(private val moviesRepo: IMoviesRepo) : ViewModel() {

    val genresState : MutableState<Result<Genres>?> = mutableStateOf(null)

    fun getGenres() {
        viewModelScope.launch {
            moviesRepo.getGenres()
                .onEach {
                    genresState.value = it
                }.collect()
        }
    }



}