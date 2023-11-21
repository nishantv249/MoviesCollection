package com.nishant.feature.ui.screens.bottom.now

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.nishant.core.db.entity.NowPlayingMovieItemEntity
import com.nishant.core.network.models.MovieItemDto
import com.nishant.core.repo.IMoviesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(private val moviesRepo: IMoviesRepo) : ViewModel() {

    private val genreIdFlow = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val nowPlayingPagingFlow = genreIdFlow.flatMapLatest { genre ->
        moviesRepo.getNowPlayingMovies(genre)
    }.cachedIn(viewModelScope)

    fun onNewGenre(genreId : String){
        genreIdFlow.value = genreId
    }

}

private fun NowPlayingMovieItemEntity.toNowPlayingMovie() : MovieItemDto{
    return MovieItemDto(adult, backdropPath, id, originalLanguage, originalTitle, overview, popularity, posterPath, releaseDate, title, video, voteAverage, voteCount)
}
