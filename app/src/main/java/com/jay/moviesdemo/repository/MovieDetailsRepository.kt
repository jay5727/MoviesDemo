package com.jay.moviesdemo.repository

import androidx.lifecycle.LiveData
import com.jay.moviesdemo.api.ApiResponse
import com.jay.moviesdemo.api.NetworkResource
import com.jay.moviesdemo.api.Resource
import com.jay.moviesdemo.model.CreditsResponseModel
import com.jay.moviesdemo.model.MoviesListResponse

import com.jay.moviesdemo.remote.MoviesService
import javax.inject.Inject

/**
 * @param movieService movie service api implementation
 */
class MovieDetailsRepository
@Inject constructor(
    private val movieService: MoviesService
) {

    fun getMovieCreditsList(movieId: String): LiveData<Resource<CreditsResponseModel>> {
        return object : NetworkResource<CreditsResponseModel>() {
            override fun fetchService(): LiveData<ApiResponse<CreditsResponseModel>> {
                return movieService.getMovieCreditsList(movieId)
            }

        }.asLiveData()
    }

    fun getSimilarMoviesList(movieId: String): LiveData<Resource<MoviesListResponse>> {
        return object : NetworkResource<MoviesListResponse>() {
            override fun fetchService(): LiveData<ApiResponse<MoviesListResponse>> {
                return movieService.getSimilarMoviesList(movieId)
            }

        }.asLiveData()
    }
}