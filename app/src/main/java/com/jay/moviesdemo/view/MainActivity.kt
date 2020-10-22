package com.jay.moviesdemo.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.jay.moviesdemo.R
import com.jay.moviesdemo.base.DataBindingActivity
import com.jay.moviesdemo.databinding.ActivityMainBinding
import com.jay.moviesdemo.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : DataBindingActivity() {

    private val binding: ActivityMainBinding by binding(R.layout.activity_main)

    private val viewModel by viewModels<MoviesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding.apply {
            lifecycleOwner = this@MainActivity
            viewModel = viewModel
        }
        viewModel.postMoviePage(1)
        attachObserver()
    }

    /**
     * Method to attach observer
     */
    private fun attachObserver() {
        viewModel.moviesLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                Toast.makeText(this, "Data Retrieved", Toast.LENGTH_LONG).show()
            }
        )
    }
}