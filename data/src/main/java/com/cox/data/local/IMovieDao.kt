package com.cox.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface IMovieDao {
    @Transaction
    suspend fun transactionUpcoming(movies: List<MovieEntity>){
        deleteUpcoming()
        insertUpcoming(movies)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpcoming(entity: List<MovieEntity>)

    @Query("DELETE FROM MovieEntity")
    fun deleteUpcoming()

    @Query("SELECT * FROM MovieEntity")
    suspend fun getUpcomingMovies(): List<MovieEntity>
}
