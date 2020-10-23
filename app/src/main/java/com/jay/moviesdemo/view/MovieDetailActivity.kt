package com.jay.moviesdemo.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.jay.moviesdemo.R
import com.jay.moviesdemo.adapter.CastAdapter
import com.jay.moviesdemo.adapter.SimilarAdapter
import com.jay.moviesdemo.api.ErrorHandling
import com.jay.moviesdemo.api.ErrorHandling.Companion.ERROR_CHECK_NETWORK_CONNECTION
import com.jay.moviesdemo.api.ErrorHandling.Companion.GENERIC_ERROR
import com.jay.moviesdemo.api.Resource
import com.jay.moviesdemo.base.DataBindingActivity
import com.jay.moviesdemo.databinding.ActivityMovieDetailBinding
import com.jay.moviesdemo.extension.applyToolbarMargin
import com.jay.moviesdemo.extension.simpleToolbarWithHome
import com.jay.moviesdemo.model.Cast
import com.jay.moviesdemo.model.Movie
import com.jay.moviesdemo.viewmodel.MovieDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailActivity : DataBindingActivity() {

    private val binding: ActivityMovieDetailBinding by binding(R.layout.activity_movie_detail)
    private lateinit var castAdapter: CastAdapter
    private lateinit var similarAdapter: SimilarAdapter

    private val mViewModel by viewModels<MovieDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@MovieDetailActivity
            viewModel =
                this@MovieDetailActivity.mViewModel.apply { postMovieId(getMovieFromIntent().movieId) }
            movie = getMovieFromIntent()
        }
        attachObserver()
        initializeUI()
    }

    private fun initializeUI() {
        applyToolbarMargin(binding.movieDetailToolbar)
        simpleToolbarWithHome(binding.movieDetailToolbar, getMovieFromIntent().originalTitle ?: "")
    }

    /**
     * Get movie from intent
     */
    private fun getMovieFromIntent(): Movie {
        return intent.getParcelableExtra<Movie>(movieId) as Movie
    }

    /**
     * Method to attach observer
     */
    private fun attachObserver() {
        mViewModel.creditListLiveData.observe(
            this,
            androidx.lifecycle.Observer { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        if (!resource.data?.cast.isNullOrEmpty()) {
                            castAdapter = CastAdapter(
                                context = this,
                                castList = resource.data?.cast?.toMutableList() as ArrayList<Cast>
                            )
                            binding.detailBody.recyclerCast.adapter = castAdapter
                        }
                    }
                    Resource.Status.ERROR -> {
                        if (ErrorHandling.isNetworkError(resource.message ?: "")) {
                            Toast.makeText(this, ERROR_CHECK_NETWORK_CONNECTION, Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(this, GENERIC_ERROR, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        )

        mViewModel.similarListLiveData.observe(
            this,
            androidx.lifecycle.Observer { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        if (!resource.data?.results.isNullOrEmpty()) {
                            similarAdapter = SimilarAdapter(
                                context = this,
                                similarMoviesList = resource.data?.results?.toMutableList() as ArrayList<Movie>
                            )
                            binding.detailBody.recyclerSimilarMovies.adapter = similarAdapter
                        }
                    }
                    Resource.Status.ERROR -> {
                        if (ErrorHandling.isNetworkError(resource.message ?: "")) {
                            Toast.makeText(this, ERROR_CHECK_NETWORK_CONNECTION, Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(this, GENERIC_ERROR, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == android.R.id.home) onBackPressed()
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {
        private const val movieId = "movie"

        /**
         * @param context object to access resource
         * @param movie movie object
         */
        fun newIntent(
            context: Context,
            movie: Movie
        ): Intent {
            return Intent(context, MovieDetailActivity::class.java).apply {
                putExtra(movieId, movie)
            }
        }
    }
}