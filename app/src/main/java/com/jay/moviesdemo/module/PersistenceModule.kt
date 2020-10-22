package com.jay.moviesdemo.module

import android.app.Application
import androidx.annotation.NonNull
import androidx.room.Room
import com.jay.moviesdemo.room.AppDatabase
import com.jay.moviesdemo.room.MovieDao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class PersistenceModule {

  @Provides
  @Singleton
  fun provideDatabase(@NonNull application: Application): AppDatabase {
    return Room
      .databaseBuilder(application, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
      .build()
  }

  @Provides
  @Singleton
  fun provideMovieDao(@NonNull database: AppDatabase): MovieDao {
    return database.getMovieDao()
  }

  //Add future DAO providers here...
}
