package com.nishant.core.domain.usecases

import com.nishant.core.data.GenreRepository
import com.nishant.core.db.entity.GenreEntity
import com.nishant.core.domain.models.Genre
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import com.google.common.truth.Truth.assertThat

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GenreUseCaseTest {

    private lateinit var genreUseCase: GenreUseCase

    @Mock
    private lateinit var genreRepository: GenreRepository

    private val genreEntityList = listOf(GenreEntity(1,"fiction"), GenreEntity(2,"thriller"))

    private val genreList = listOf(Genre(1,"fiction"), Genre(2,"thriller"))
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        genreUseCase = GenreUseCase(genreRepository)
    }

    @Test
    fun getGenreListMappedToDomain() = runTest {
        Mockito.`when`(genreRepository.getGenre()).thenReturn(flow {
            emit(genreEntityList)
        })
        val result = genreUseCase().first()
        assertThat(result).isEqualTo(genreList)
    }

}