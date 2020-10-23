package com.jay.moviesdemo.view

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import com.jay.moviesdemo.R
import com.jay.moviesdemo.adapter.MoviesAdapter
import com.jay.moviesdemo.api.ErrorHandling
import com.jay.moviesdemo.api.Resource
import com.jay.moviesdemo.base.DataBindingActivity
import com.jay.moviesdemo.databinding.ActivityMoviesBinding
import com.jay.moviesdemo.extension.hideKeyboard
import com.jay.moviesdemo.listener.ItemClickListener
import com.jay.moviesdemo.model.Movie
import com.jay.moviesdemo.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesActivity : DataBindingActivity(), ItemClickListener {

    private val binding: ActivityMoviesBinding by binding(R.layout.activity_movies)
    private lateinit var adapter: MoviesAdapter

    private val viewModel by viewModels<MoviesViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@MoviesActivity
            viewModel = viewModel
        }
        setUpSearch()
        attachObserver()
        viewModel.postMoviePage(page = 1)

    }


    /**
     * Sets up Search bar for movie search
     */
    private fun setUpSearch() {
        binding.etSearch.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard(textView, this)
                viewModel.searchQuery.value = textView.text.toString()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.btnClear.setOnClickListener {
            binding.etSearch.setText("")
            viewModel.searchQuery.value = ""
        }
    }

    /**
     * Method to attach observer
     */
    private fun attachObserver() {
        viewModel.showSearchBox.observe(this, androidx.lifecycle.Observer {
            if (it) {
                binding.searchBar.visibility = View.VISIBLE
            } else {
                binding.searchBar.visibility = View.GONE
            }
        })

        viewModel.moviesLiveData.observe(
            this,
            androidx.lifecycle.Observer { resource ->
                when (resource.status) {
                    Resource.Status.LOADING -> {
                        viewModel.shouldShowLoader.set(true)
                    }
                    Resource.Status.SUCCESS -> {
                        viewModel.shouldShowLoader.set(false)
                        viewModel.showSearchBox.value = true
                        if (!resource.data.isNullOrEmpty()) {
                            adapter = MoviesAdapter(
                                context = this,
                                moviesList = resource.data.toMutableList() as ArrayList<Movie>
                            )
                            binding.adapter = adapter
                            adapter?.setOnItemClickListener(this)
                        }
                    }
                    Resource.Status.ERROR -> {
                        viewModel.shouldShowLoader.set(true)
                        if (ErrorHandling.isNetworkError(resource.message ?: "")) {
                            Toast.makeText(
                                this,
                                ErrorHandling.ERROR_CHECK_NETWORK_CONNECTION,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(this, ErrorHandling.GENERIC_ERROR, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        )
        viewModel.filteredMoviesLiveData.observe(this, androidx.lifecycle.Observer {
            it?.let {
                adapter = MoviesAdapter(
                    context = this,
                    moviesList = it.toMutableList() as ArrayList<Movie>
                )
            } ?: kotlin.run {
                adapter = MoviesAdapter(
                    context = this,
                    moviesList = viewModel.moviesLiveData.value?.data?.toMutableList() as ArrayList<Movie>
                )
            }
            binding.adapter = adapter
            adapter?.setOnItemClickListener(this)
        })

        viewModel.searchQuery.observe(this, androidx.lifecycle.Observer {
            if (!it.isNullOrEmpty()) {
                viewModel.getFilteredMovies(it)
            } else {
                viewModel.setFilteredListEmpty()
            }
        })
    }

    override fun onItemClicked(movie: Movie?) {
        movie?.let {
            startActivity(MovieDetailActivity.newIntent(this, movie))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter?.setOnItemClickListener(null)
    }
}