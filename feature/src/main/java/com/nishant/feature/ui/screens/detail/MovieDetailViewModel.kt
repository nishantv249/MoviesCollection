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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.combine

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val moviesRepo : IMoviesRepo) : ViewModel() {

    private val _movieDetailFlow : MutableStateFlow<MovieDetailDto?> = MutableStateFlow(null)
    private val _artistsFlow : MutableStateFlow<ArtistDto?> = MutableStateFlow(null)

    private val artistsFlow = _artistsFlow.filter { it != null }.asResult()

    private val movieDetailFlow = _movieDetailFlow.filter { it != null }.asResult()

    fun getMovieDetail(id : Int){
        viewModelScope.launch {
            _movieDetailFlow.value = moviesRepo.getMovieDetail(id)
        }
    }

    private val _recommendedMovieFlow :MutableStateFlow<MoviesDto?> = MutableStateFlow(null)

    private val recommendedMovieFlow = _recommendedMovieFlow.filter { it!= null}.asResult()

    val movieDetail = combine(
        movieDetailFlow,
        recommendedMovieFlow,
        artistsFlow
    ){ movieDetails, recommendedMovie,artist ->
        if(movieDetails is com.nishant.core.repo.LoadingState.Success && recommendedMovie is LoadingState.Success && artist is LoadingState.Success){
            if((movieDetails.t != null) && (recommendedMovie.t != null) && (artist.t != null)) {
                LoadingState.Success(MovieDetailUiState(
                    movieDetails.t!!, recommendedMovie.t!!.results,
                    artist.t!!.cast
                ))
            }else{
                LoadingState.Error("")
            }
        }else if(movieDetails is LoadingState.Error){
            LoadingState.Error(movieDetails.e)
        } else if(recommendedMovie is LoadingState.Error) {
            LoadingState.Error(recommendedMovie.e)
        }else if(artist is LoadingState.Error){
            LoadingState.Error(artist.e)
        }else{
            LoadingState.Loading
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly,LoadingState.Loading)

    fun getRecommendedMovies(movieId : Int){
        viewModelScope.launch {
           _recommendedMovieFlow.value = moviesRepo.getRecommendedMovies(movieId)
        }
    }

    fun getCredits(movieId: Int){
        viewModelScope.launch {
            _artistsFlow.value = moviesRepo.getCredits(movieId)
        }
    }
}

@Stable
data class MovieDetailUiState(val movieDetail: MovieDetailDto,val movieList : List<MovieItemDto>,val cast : List<CastDto>)
