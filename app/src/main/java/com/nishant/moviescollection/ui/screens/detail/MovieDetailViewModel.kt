package com.nishant.moviescollection.ui.screens.detail

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nishant.moviescollection.network.Result
import com.nishant.moviescollection.network.asResult
import com.nishant.moviescollection.network.models.*
import com.nishant.moviescollection.repo.IMoviesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.combine

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val moviesRepo : IMoviesRepo) : ViewModel() {

    private val _movieDetailFlow : MutableStateFlow<MovieDetail?> = MutableStateFlow(null)
    private val _artistsFlow : MutableStateFlow<Artist?> = MutableStateFlow(null)

    private val artistsFlow = _artistsFlow.filter { it != null }.asResult()

    private val movieDetailFlow = _movieDetailFlow.filter { it != null }.asResult()

    fun getMovieDetail(id : Int){
        viewModelScope.launch {
            _movieDetailFlow.value = moviesRepo.getMovieDetail(id)
        }
    }

    private val _recommendedMovieFlow :MutableStateFlow<BaseModel?> = MutableStateFlow(null)

    private val recommendedMovieFlow = _recommendedMovieFlow.filter { it!= null}.asResult()

    val movieDetail = combine(
        movieDetailFlow,
        recommendedMovieFlow,
        artistsFlow
    ){ movieDetails, recommendedMovie,artist ->
        if(movieDetails is Result.Success && recommendedMovie is Result.Success && artist is Result.Success){
            if((movieDetails.t != null) && (recommendedMovie.t != null) && (artist.t != null)) {
                Result.Success(MovieDetailUiState(
                    movieDetails.t, recommendedMovie.t.results,
                    artist.t.cast
                ))
            }else{
                Result.Error("")
            }
        }else if(movieDetails is Result.Error){
            Result.Error(movieDetails.e)
        } else if(recommendedMovie is Result.Error) {
            Result.Error(recommendedMovie.e)
        }else if(artist is Result.Error){
            Result.Error(artist.e)
        }else{
            Result.Loading
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly,Result.Empty)

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
data class MovieDetailUiState(val movieDetail: MovieDetail,val movieList : List<MovieItem>,val cast : List<Cast>)
