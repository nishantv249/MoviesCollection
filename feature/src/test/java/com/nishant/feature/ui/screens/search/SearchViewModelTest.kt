package com.nishant.feature.ui.screens.search

import com.nishant.core.repo.IMoviesRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import com.google.common.truth.Truth.assertThat
import com.nishant.core.network.models.MoviesDto
import com.nishant.core.repo.LoadingState
import com.nishant.feature.MainCoroutineRule
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class SearchViewModelTest {

    @get: Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var moviesRepo : IMoviesRepo

    private lateinit var searchViewModel: SearchViewModel

    private val moviesResponse = MoviesDto(1, emptyList(),1,3)

    private val searchQuery = "movie"

    private val errorQuery = "error"

    private val errorMessage = "Runtime error"

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        searchViewModel = SearchViewModel(moviesRepo)
    }

    @Test
    fun getSearchResultFlowWithEmptyState() = runTest {
       val loadingState = searchViewModel.searchResultFlow.first()
        assertThat(loadingState).isEqualTo(LoadingState.Empty)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getSearchResultForSuccessFailureState() = runTest {
        Mockito.`when`(moviesRepo.search(searchQuery))
            .thenReturn(flowOf(LoadingState.Success(moviesResponse)))
        val list = mutableListOf<LoadingState<MoviesDto>>()
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            searchViewModel.searchResultFlow.take(3).toList(list)
        }
        searchViewModel.search(searchQuery)
        advanceTimeBy(400)
        Mockito.`when`(moviesRepo.search(errorQuery))
            .thenReturn(flowOf(LoadingState.Error(errorMessage)))
        searchViewModel.search(errorQuery)
        advanceTimeBy(400)
        assertThat(list).isEqualTo(
            listOf(
                LoadingState.Empty,
                LoadingState.Success(moviesResponse),
                LoadingState.Error(errorMessage)
            )
        )
    }

}