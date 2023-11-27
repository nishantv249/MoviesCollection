package com.nishant.core.repo

import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import com.nishant.core.MainCoroutineRule
import com.nishant.core.network.models.MovieItemDto
import com.nishant.core.network.models.MoviesDto
import com.nishant.core.repo.paging.MoviesDataSource
import kotlinx.coroutines.test.runTest


import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MoviesDataSourceTest {

    @get: Rule
    val mainCoroutineRule = MainCoroutineRule()


    private lateinit var moviesDataSource: MoviesDataSource

    private val genreId = ""

    private val moviesItemList = listOf(MovieItemDto(false,"",1,"en"
                        ,"title","overview",2.5,"","","title",
                        false,1.2,45))

    private val nowPlayingMoviesResult = MoviesDto(1, moviesItemList,1,1)

    @Before
    fun setUp() {
        moviesDataSource = MoviesDataSource{
            nowPlayingMoviesResult.results
        }
    }

    @Test
    fun load() = runTest {
       val result = moviesDataSource.load(PagingSource.LoadParams
                    .Append<Int>(1,20,false))
        assertThat(result).isEqualTo(PagingSource.LoadResult.Page(moviesItemList,null,2))
    }

}