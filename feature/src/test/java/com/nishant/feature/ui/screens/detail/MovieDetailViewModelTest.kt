package com.nishant.feature.ui.screens.detail

import com.nishant.core.network.models.ArtistDto
import com.nishant.core.network.models.MovieDetailDto
import com.nishant.core.network.models.MovieItemDto
import com.nishant.core.network.models.MoviesDto
import com.nishant.core.repo.IMoviesRepo
import com.nishant.core.repo.LoadingState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import com.google.common.truth.Truth.assertThat
import com.nishant.feature.MainCoroutineRule

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class MovieDetailViewModelTest {

    @get:Rule
    var mainCoroutineRule  = MainCoroutineRule()

    private lateinit var iMoviesRepo: IMoviesRepo

    private lateinit var movieDetailViewModel: MovieDetailViewModel

    private val movieId = 12

    private val movieDetailDto = MovieDetailDto(false,"",12, emptyList(),
                                    "home_page",13,"imdbId","en",
                            "title","",1.2,"",
                                "12/12/12",123,11,"","",
                                    "title",false,1.3,11)

    private val moviesItemList = listOf(
        MovieItemDto(false,"",1,"en"
        ,"title","overview",2.5,"","","title",
        false,1.2,45)
    )

    private val moviesDto = MoviesDto(1,moviesItemList,2,2)

    private val artistDto = ArtistDto(emptyList(),1)

    @Before
    fun setUp() {
        iMoviesRepo = Mockito.mock(IMoviesRepo::class.java)
        movieDetailViewModel = MovieDetailViewModel(iMoviesRepo)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getMovieDetail() = runTest {
        Mockito.`when`(iMoviesRepo.getMovieDetail(movieId)).thenReturn(flow{
            emit(LoadingState.Success(movieDetailDto))
        })

        Mockito.`when`(iMoviesRepo.getRecommendedMovies(movieId)).thenReturn(flow {
            emit(LoadingState.Success(moviesDto))
        })

        Mockito.`when`(iMoviesRepo.getCredits(movieId)).thenReturn(flow {
            emit(LoadingState.Success(artistDto))
        })

        var collectedState : LoadingState<MovieDetailUiState>? = null
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            collectedState = movieDetailViewModel.movieDetail.drop(1).first()
        }

        movieDetailViewModel.getMovieDetail(movieId)
        movieDetailViewModel.getRecommendedMovies(movieId)
        movieDetailViewModel.getCredits(movieId = movieId)
        assertThat(collectedState).isEqualTo(LoadingState.Success(MovieDetailUiState(movieDetailDto,
                moviesItemList, emptyList()
        )))
    }

}