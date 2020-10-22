package com.jay.moviesdemo.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.jay.moviesdemo.api.AbsentLiveData
import com.jay.moviesdemo.api.Resource
import com.jay.moviesdemo.base.BaseViewModel
import com.jay.moviesdemo.model.Movie
import com.jay.moviesdemo.repository.MoviesRepository
import timber.log.Timber

/**
 * MoviesViewModel - view model for movies list inherits [BaseViewModel]
 */
class MoviesViewModel @ViewModelInject constructor(
    private val repository: MoviesRepository
) : BaseViewModel() {

    val TAG = MoviesViewModel::class.java.simpleName

    private var moviePageLiveData: MutableLiveData<Int> = MutableLiveData()
    val moviesLiveData: LiveData<Resource<List<Movie>>>

    init {
        Timber.d("Injection : MoviesViewModel")
        moviesLiveData = moviePageLiveData.switchMap {
            moviePageLiveData.value?.let { repository.fetchNowPlayingMoviesList(pageNo = it) }
                ?: AbsentLiveData.create()
        }
    }

    fun postMoviePage(page: Int) = moviePageLiveData.postValue(page)

}