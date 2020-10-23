package com.jay.moviesdemo.viewmodel

import android.database.sqlite.SQLiteQueryBuilder
import androidx.databinding.ObservableBoolean
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.sqlite.db.SimpleSQLiteQuery
import com.jay.moviesdemo.api.AbsentLiveData
import com.jay.moviesdemo.api.Resource
import com.jay.moviesdemo.base.BaseViewModel
import com.jay.moviesdemo.model.Movie
import com.jay.moviesdemo.repository.MoviesRepository
import com.jay.moviesdemo.util.SingleLiveEvent
import kotlinx.coroutines.launch

/**
 * MoviesViewModel - view model for movies list inherits [BaseViewModel]
 */
class MoviesViewModel @ViewModelInject constructor(
    private val repository: MoviesRepository
) : BaseViewModel() {

    val searchQuery = SingleLiveEvent<String>()
    val showSearchBox = MutableLiveData(false)
    val shouldShowLoader = ObservableBoolean(true)

    val TAG = MoviesViewModel::class.java.simpleName

    private var moviePageLiveData: MutableLiveData<Int> = MutableLiveData()
    val moviesLiveData: LiveData<Resource<List<Movie>>>

    private val _filteredMoviesLiveData = MutableLiveData<List<Movie>>()
    val filteredMoviesLiveData: LiveData<List<Movie>>
        get() = _filteredMoviesLiveData


    init {
        moviesLiveData = moviePageLiveData.switchMap {
            moviePageLiveData.value?.let { repository.fetchNowPlayingMoviesList(pageNo = it) }
                ?: AbsentLiveData.create()
        }
    }

    fun postMoviePage(page: Int) = moviePageLiveData.postValue(page)

    /**
     *
     * @param query filter the movie list as per query
     */
    fun getFilteredMovies(query: String) {
        viewModelScope.launch {
            val splittedArray = query.split(" ")
            val queryString = StringBuilder("")
            splittedArray.forEach {
                if (queryString.isNotEmpty()) {
                    queryString.append(" OR ")
                }
                queryString.append(
                    "${Movie.COLUMN_NAME} LIKE '$it%'  OR " +
                            "${Movie.COLUMN_NAME} LIKE '% $it'"
                )
            }
            val queryBuilder = SQLiteQueryBuilder()
            queryBuilder.appendWhere(queryString)
            queryBuilder.tables = Movie.TABLE_NAME
            val data = queryBuilder.buildQuery(null, null, null, null, null, null)
            val sqLiteQuery = SimpleSQLiteQuery(data)
            _filteredMoviesLiveData.value = repository.getFilteredMovies(sqLiteQuery)
        }
    }

    /**
     * sets filter data list to null
     */
    fun setFilteredListEmpty() {
        _filteredMoviesLiveData.value = null
    }
}