package com.jay.moviesdemo.adapter

import android.widget.ImageView
import android.widget.RatingBar
import androidx.databinding.BindingAdapter
import coil.api.load
import coil.transform.CircleCropTransformation
import com.jay.moviesdemo.BuildConfig
import com.jay.moviesdemo.R


/**
 * Consists of all binding adapters used in the layout file
 */
@BindingAdapter("loadSmallImage")
fun loadSmallImage(imageView: ImageView, url: String?) {
    url?.let {
        imageView.load(BuildConfig.SMALL_IMAGE_URL + it) {
            placeholder(R.drawable.random_image)
            error(R.drawable.ic_teared)
        }
    } ?: imageView.setImageResource(R.drawable.ic_teared)
}

@BindingAdapter("loadCircularImage")
fun loadCircularImage(imageView: ImageView, url: String?) {
    url?.let {
        imageView.load(BuildConfig.SMALL_IMAGE_URL + it) {
            placeholder(R.drawable.random_image)
            error(R.drawable.ic_teared)
            transformations(CircleCropTransformation())
        }
    } ?: imageView.setImageResource(R.drawable.ic_teared)
}

@BindingAdapter("loadLargeImage")
fun loadLargeImage(imageView: ImageView, url: String?) {
    url?.let {
        imageView.load(BuildConfig.LARGE_IMAGE_URL + it) {
            placeholder(R.drawable.random_image)
            error(R.drawable.ic_teared)
        }
    } ?: imageView.setImageResource(R.drawable.ic_teared)
}

@BindingAdapter("setRating")
fun setRating(ratingBar: RatingBar, vote: Double) {
    ratingBar.rating = (vote / 2).toFloat()
}