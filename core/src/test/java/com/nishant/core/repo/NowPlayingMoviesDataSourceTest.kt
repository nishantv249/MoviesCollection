package com.nishant.core.repo

import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import com.nishant.core.MainCoroutineRule
import com.nishant.core.network.api.MoviesApiService
import com.nishant.core.network.models.MoviesDto
import com.nishant.core.repo.paging.NowPlayingMoviesDataSource
import kotlinx.coroutines.test.runTest


import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class NowPlayingMoviesDataSourceTest {

    @get: Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var apiService: MoviesApiService

    lateinit var nowPlayingMoviesDataSource: NowPlayingMoviesDataSource

    val genreId = ""

    val nowPlayingList = MoviesDto(1, emptyList(),1,1)

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        nowPlayingMoviesDataSource = NowPlayingMoviesDataSource(genreId,apiService)
    }

    @Test
    fun load() = runTest {
        Mockito.`when`(apiService.nowPlayingMovieList(1,genreId)).thenReturn(nowPlayingList)

       val result = nowPlayingMoviesDataSource.load(PagingSource.LoadParams
                    .Append<Int>(1,20,false))
        assertThat(result).isEqualTo(PagingSource.LoadResult.Page(emptyList(),null,2))
    }

}