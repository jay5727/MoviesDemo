package com.jay.moviesdemo.view

import android.os.Bundle
import com.jay.moviesdemo.R
import com.jay.moviesdemo.base.DataBindingActivity
import com.jay.moviesdemo.databinding.ActivityMainBinding

class MainActivity : DataBindingActivity() {

    private val binding: ActivityMainBinding by binding(R.layout.activity_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}