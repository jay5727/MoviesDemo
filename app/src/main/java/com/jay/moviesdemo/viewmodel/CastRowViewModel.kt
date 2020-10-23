package com.jay.moviesdemo.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.jay.moviesdemo.model.Cast

/**
 * View model for row item of Cast List inherits [ViewModel] 
 */
class CastRowViewModel : ViewModel(){
    val item = ObservableField<Cast>()
}