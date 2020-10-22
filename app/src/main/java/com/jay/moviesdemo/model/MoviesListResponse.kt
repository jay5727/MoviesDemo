package com.jay.moviesdemo.model

import com.google.gson.annotations.SerializedName

/**
 * Response for Movies
 */
open class MoviesListResponse(
    @SerializedName("page") val page: Int? = null,
    @SerializedName("total_results") val totalResults: Int? = null,
    @SerializedName("total_pages") val totalPages: Int? = null,
    @SerializedName("results") var results: ArrayList<Movie>? = null
) : NetworkResponseModel
