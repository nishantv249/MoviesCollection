package com.nishant.feature.ui.screens.detail

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nishant.core.network.models.ArtistDto
import com.nishant.core.network.models.CastDto
import com.nishant.core.network.models.MovieDetailDto
import com.nishant.core.network.models.MovieItemDto
import com.nishant.core.network.models.MoviesDto
import com.nishant.core.repo.IMoviesRepo
import com.nishant.core.repo.LoadingState
import com.nishant.core.repo.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.combine

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val moviesRepo : IMoviesRepo) : ViewModel() {

    private val _movieDetailFlow : MutableStateFlow<Int?> = MutableStateFlow(null)

    private val _artistsFlow : MutableStateFlow<Int?> = MutableStateFlow(null)

    private val _recommendedMovieFlow :MutableStateFlow<Int?> = MutableStateFlow(null)


    @OptIn(ExperimentalCoroutinesApi::class)
    private val artistsFlow = _artistsFlow.filter { it != null }
        .flatMapLatest { moviesRepo.getCredits(it!!) }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val movieDetailFlow = _movieDetailFlow.filter { it != null }
        .flatMapLatest { moviesRepo.getMovieDetail(it!!) }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val recommendedMovieFlow = _recommendedMovieFlow.filter { it!= null}
        .flatMapLatest { moviesRepo.getRecommendedMovies(it!!) }

    val movieDetail = combine(
        movieDetailFlow,
        recommendedMovieFlow,
        artistsFlow
    ){ movieDetails, recommendedMovie,artist ->
        if(movieDetails is com.nishant.core.repo.LoadingState.Success && recommendedMovie is LoadingState.Success && artist is LoadingState.Success){
            LoadingState.Success(MovieDetailUiState(
                movieDetails.t, recommendedMovie.t.results,
                artist.t.cast
            ))
        }else if(movieDetails is LoadingState.Error){
            LoadingState.Error(movieDetails.e)
        } else if(recommendedMovie is LoadingState.Error) {
            LoadingState.Error(recommendedMovie.e)
        }else if(artist is LoadingState.Error){
            LoadingState.Error(artist.e)
        }else{
            LoadingState.Loading
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily,LoadingState.Loading)

    fun getRecommendedMovies(movieId : Int){
        _recommendedMovieFlow.value = movieId
    }

    fun getCredits(movieId: Int){
        _artistsFlow.value = movieId
    }

    fun getMovieDetail(id : Int){
        _movieDetailFlow.value = id
    }

}

@Stable
data class MovieDetailUiState(val movieDetail: MovieDetailDto,val movieList : List<MovieItemDto>,
                              val cast : List<CastDto>)
