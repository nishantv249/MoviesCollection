package com.nishant.core.data

import com.nishant.core.db.entity.GenreEntity
import com.nishant.core.db.entity.NowPlayingMovieItemEntity
import com.nishant.core.network.models.GenreDto
import com.nishant.core.network.models.MovieItemDto

fun GenreDto.toEntity() : GenreEntity {
    return GenreEntity(id = id,name = name)
}

fun MovieItemDto.toNowPlayingMovieEntity() : NowPlayingMovieItemEntity {
    return NowPlayingMovieItemEntity(id = id, adult = adult, backdropPath = backdropPath,
        originalLanguage = originalLanguage, originalTitle = originalTitle, overview = overview,
        popularity = popularity, posterPath = posterPath, releaseDate = releaseDate, title = title,
        video = video, voteAverage = voteAverage, voteCount = voteCount)
}
