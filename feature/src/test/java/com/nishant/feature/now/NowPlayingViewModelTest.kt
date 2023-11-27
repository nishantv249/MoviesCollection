package com.nishant.feature.now

import androidx.paging.PagingData
import com.google.common.truth.Truth.assertThat
import com.nishant.core.network.models.MovieItemDto
import com.nishant.core.repo.IMoviesRepo
import com.nishant.feature.MainCoroutineRule
import com.nishant.feature.ui.screens.bottom.now.NowPlayingViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest


import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class NowPlayingViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var moviesRepo: IMoviesRepo

    private lateinit var nowPlayingViewModel: NowPlayingViewModel

    @Before
    fun setUp() {
        moviesRepo = Mockito.mock(IMoviesRepo::class.java)
        nowPlayingViewModel = NowPlayingViewModel(moviesRepo)
    }

    @Test
    fun getNowPlayingPagingFlow() = runTest{
        val emptyData = PagingData.from<MovieItemDto>(emptyList())
        Mockito.`when`(moviesRepo.getNowPlayingMovies("")).thenReturn(flowOf(emptyData))
        nowPlayingViewModel.onNewGenre("")
        nowPlayingViewModel.nowPlayingPagingFlow.collect { list ->
            assertThat(list).isEqualTo(emptyData)
        }
    }

    @Test
    fun onNewGenre() {
    }
}