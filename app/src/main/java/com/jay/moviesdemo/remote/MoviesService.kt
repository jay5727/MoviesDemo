package com.jay.moviesdemo.remote

import androidx.lifecycle.LiveData
import com.jay.moviesdemo.api.ApiResponse
import com.jay.moviesdemo.model.MoviesListResponse


import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesService {

    /**
     * Get the list of Movies object.
     *
     * @param page Specify the page of results to query.
     *
     * @return [MoviesListResponse] response
     */
    @GET("movie/now_playing")
    fun getMovieList(@Query("page") page: Int): LiveData<ApiResponse<MoviesListResponse>>
}
