package com.jay.moviesdemo.repository

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.jay.moviesdemo.api.ApiResponse
import com.jay.moviesdemo.api.NetworkBoundResource
import com.jay.moviesdemo.api.Resource
import com.jay.moviesdemo.model.Movie
import com.jay.moviesdemo.model.MoviesListResponse
import com.jay.moviesdemo.remote.MoviesService
import com.jay.moviesdemo.room.MovieDao
import javax.inject.Inject

/**
 * @param movieDao DAO object to perform DB operations
 * @param movieService movie service api implementation
 */
class MoviesRepository
@Inject constructor(
    private val movieDao: MovieDao,
    private val movieService: MoviesService
) {

    fun fetchNowPlayingMoviesList(pageNo: Int): LiveData<Resource<List<Movie>>> {
        return object : NetworkBoundResource<List<Movie>, MoviesListResponse>() {

            override fun saveFetchData(response: MoviesListResponse) {
                response.results?.let { movies ->
                    for (movie in movies) {
                        movieDao.insert(
                            Movie(
                                movieId = movie.movieId,
                                originalTitle = movie.originalTitle,
                                overview = movie.overview,
                                voteAverage = movie.voteAverage,
                                releaseDate = movie.releaseDate,
                                posterPath = movie.posterPath,
                                backdropPath = movie.backdropPath,
                                popularity = movie.popularity,
                                voteCount = movie.voteCount,
                                timestampInMillis = movie.timestampInMillis
                            )
                        )
                    }
                }
            }

            override fun fetchService(): LiveData<ApiResponse<MoviesListResponse>> {
                return movieService.getMovieList(page = pageNo)
            }

            override fun shouldFetch(data: List<Movie>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun loadFromDb(): LiveData<List<Movie>> {
                return movieDao.getMovies()
            }

            override fun onFetchFailed(message: String?) {
                // no-op
            }
        }.asLiveData()
    }

    suspend fun getFilteredMovies( query: SupportSQLiteQuery) : List<Movie> {
        return movieDao.getFilteredMovies(query)
    }
}