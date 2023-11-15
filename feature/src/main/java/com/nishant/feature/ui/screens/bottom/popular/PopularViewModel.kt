package com.nishant.feature.ui.screens.bottom.popular

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
class PopularViewModel @Inject constructor  (private val moviesRepo: IMoviesRepo) : ViewModel() {

    private val genreFlow = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingFLow = genreFlow.flatMapLatest { moviesRepo.getPopularMovies(it) }.cachedIn(viewModelScope)

    fun onNewGenreChange(genreId : String){
        genreFlow.value = genreId
    }

}