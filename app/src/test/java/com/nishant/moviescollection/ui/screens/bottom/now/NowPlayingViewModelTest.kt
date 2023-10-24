package com.nishant.moviescollection.ui.screens.bottom.now

import androidx.paging.PagingData
import com.nishant.moviescollection.MainCoroutineRule
import com.nishant.moviescollection.network.models.MovieItem
import com.nishant.moviescollection.repo.IMoviesRepo
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest


import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class NowPlayingViewModelTest {

    @get:Rule
    val mainCoroutineRule : MainCoroutineRule = MainCoroutineRule()

    lateinit var moviesRepo: IMoviesRepo

    lateinit var nowPlayingViewModel: NowPlayingViewModel

    @Before
    fun setUp() {
        moviesRepo = Mockito.mock(IMoviesRepo::class.java)
        nowPlayingViewModel = NowPlayingViewModel(moviesRepo)
    }

    @Test
    fun getNowPlayingPagingFlow() = runTest{
        val emptyData = PagingData.from<MovieItem>(emptyList())
        Mockito.`when`(moviesRepo.getNowPlayingMovies("")).thenReturn(flow{
            emit(emptyData)
        })
        val list = nowPlayingViewModel.nowPlayingPagingFlow.first()
        assertThat(list).isEqualTo(emptyData)
    }

    @Test
    fun onNewGenre() {
    }
}