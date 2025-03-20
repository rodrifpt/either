package com.cox.data.mapper

import com.cox.data.local.MovieEntity
import com.cox.domain.model.ResultResponse

fun ResultResponse.toEntity(): MovieEntity {
    return MovieEntity(
        id = this.id,
        backdropPath = this.backdrop_path,
        releaseDate = this.release_date,
        originalLanguage = this.original_language,
        originalTitle = this.original_title,
        overview = this.overview,
        voteAverage = this.vote_average
    )
}

fun MovieEntity.toDomain(): ResultResponse {
    return ResultResponse(
        id = this.id,
        backdrop_path = this.backdropPath,
        release_date = this.releaseDate,
        original_language = this.originalLanguage,
        original_title = this.originalTitle,
        overview = this.overview,
        vote_average = this.voteAverage
    )
}
