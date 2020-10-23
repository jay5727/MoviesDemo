package com.jay.moviesdemo.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.jay.moviesdemo.base.BaseViewModel
import com.jay.moviesdemo.model.Movie

/** 
 * View model for row item of Movie List inherits [ViewModel] 
 */
class MovieRowViewModel : BaseViewModel() {
    val item = ObservableField<Movie>()
}