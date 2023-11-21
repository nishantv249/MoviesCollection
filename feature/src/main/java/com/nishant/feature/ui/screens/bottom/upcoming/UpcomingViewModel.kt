package com.nishant.feature.ui.screens.bottom.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.nishant.core.repo.IMoviesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class UpcomingViewModel @Inject constructor(moviesRepo : IMoviesRepo) : ViewModel() {

    private val genreIdFlow = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val upcomingMovieFlow = genreIdFlow.flatMapLatest { genre ->
        moviesRepo.getUpcomingMovies(genre)
    }.cachedIn(viewModelScope)

    fun onNewGenre(genreId : String){
        genreIdFlow.value = genreId
    }

}