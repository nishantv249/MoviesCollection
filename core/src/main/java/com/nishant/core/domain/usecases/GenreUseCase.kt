package com.nishant.core.domain.usecases

import com.nishant.core.data.GenreRepository
import com.nishant.core.db.entity.GenreEntity
import com.nishant.core.domain.models.Genre
import com.nishant.core.domain.toGenre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import javax.inject.Inject

class GenreUseCase @Inject constructor(private val genreRepository: GenreRepository) {

    operator fun invoke() : Flow<List<Genre>> {
        return genreRepository.getGenre().map { list ->
            list.map { genreEntity ->
                genreEntity.toGenre()
            }
        }
    }
}

