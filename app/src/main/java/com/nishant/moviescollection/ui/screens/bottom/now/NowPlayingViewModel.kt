package com.nishant.moviescollection.ui.screens.bottom.now

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.nishant.moviescollection.repo.IMoviesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(private val moviesRepo: IMoviesRepo) : ViewModel() {

    private val genreIdFlow = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val nowPlayingPagingFlow = genreIdFlow.flatMapLatest {
        moviesRepo.getNowPlayingMovies(it) }
                                .cachedIn(viewModelScope)

    fun onNewGenre(genreId : String){
        viewModelScope.launch {
            genreIdFlow.value = genreId
        }
    }

}