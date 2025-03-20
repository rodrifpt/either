package com.cox.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MovieEntity")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val backdropPath: String,
    val releaseDate: String,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val voteAverage: Double
)
