package com.jay.moviesdemo.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.jay.moviesdemo.api.AbsentLiveData
import com.jay.moviesdemo.api.Resource
import com.jay.moviesdemo.model.CreditsResponseModel
import com.jay.moviesdemo.model.MoviesListResponse
import com.jay.moviesdemo.repository.MovieDetailsRepository

class MovieDetailsViewModel @ViewModelInject constructor(
    private val repository: MovieDetailsRepository
) : ViewModel() {

    private val movieIdLiveData: MutableLiveData<Int> = MutableLiveData()

    val creditListLiveData: LiveData<Resource<CreditsResponseModel>>
    val similarListLiveData: LiveData<Resource<MoviesListResponse>>

    init {
        this.creditListLiveData = movieIdLiveData.switchMap {
            movieIdLiveData.value?.let {
                repository.getMovieCreditsList(movieId = it.toString())
            } ?: AbsentLiveData.create()
        }

        this.similarListLiveData = movieIdLiveData.switchMap {
            movieIdLiveData.value?.let {
                repository.getSimilarMoviesList(movieId = it.toString())
            } ?: AbsentLiveData.create()
        }

    }

    fun postMovieId(id: Int?) = movieIdLiveData.postValue(id)
}

