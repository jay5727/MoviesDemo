package com.jay.moviesdemo.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jay.moviesdemo.model.Movie


/**
 * AppDatabase class to handle CRUD related operations
 * Include the list of entities associated with the database within the annotation.
 * Contains Abstract DAO methods
 */
@Database(entities = [Movie::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

    companion object {
        const val DATABASE_NAME = "Movie.db"
    }

}