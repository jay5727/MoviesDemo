package com.jay.moviesdemo.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Query
import com.jay.moviesdemo.model.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie)

    @Query("SELECT * FROM movies_table")
    fun getMovies(): LiveData<List<Movie>>

}