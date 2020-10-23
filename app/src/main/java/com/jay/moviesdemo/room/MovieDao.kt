package com.jay.moviesdemo.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Query
import androidx.sqlite.db.SupportSQLiteQuery
import com.jay.moviesdemo.model.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie)

    @Query("SELECT * FROM movies_table")
    fun getMovies(): LiveData<List<Movie>>

    @RawQuery(observedEntities = [Movie::class])
    fun getFilteredMovies(
        query: SupportSQLiteQuery
    ) : LiveData<List<Movie>>

}