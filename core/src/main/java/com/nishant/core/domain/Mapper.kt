package com.nishant.core.domain

import com.nishant.core.db.entity.GenreEntity
import com.nishant.core.domain.models.Genre

fun GenreEntity.toGenre()  : Genre {
    return Genre(id = id,name = name)
}
