package com.nishant.core.data

import com.nishant.core.network.api.MoviesApiService
import com.google.common.truth.Truth.assertThat
import com.nishant.core.MainCoroutineRule
import com.nishant.core.db.dao.FakeGenreDao
import com.nishant.core.db.entity.GenreEntity
import com.nishant.core.network.models.GenreDto
import com.nishant.core.network.models.GenresDto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class OfflineFirstGenreRepoTest {

    @get: Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var offlineFirstGenreRepo: OfflineFirstGenreRepo

    private  val  genreDao = FakeGenreDao()

    @Mock
    private lateinit var moviesApiService: MoviesApiService

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        offlineFirstGenreRepo = OfflineFirstGenreRepo(genreDao, moviesApiService)
    }

    @Test
    fun getEmptyGenreList() = runTest {
        Mockito.`when`(moviesApiService.genreList()).thenReturn(GenresDto(emptyList()))
        val list = offlineFirstGenreRepo.getGenre().first()
        assertThat(list).isEqualTo(emptyList<GenreEntity>())
    }


    @Test
    fun getGenreList() = runTest {
        val genreDtoList = listOf(GenreDto(1,"fiction"))
        Mockito.`when`(moviesApiService.genreList()).thenReturn(GenresDto(genreDtoList))
        backgroundScope.launch {
            val  collectedItems = offlineFirstGenreRepo.getGenre().toList()
            assertThat(listOf(emptyList(), listOf(GenreEntity(1,"fiction"))))
                .isEqualTo(collectedItems)
        }
    }

}