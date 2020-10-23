package com.jay.moviesdemo.listener

import com.jay.moviesdemo.model.Movie

/**
 * An interface to track the clicks
 */
interface ItemClickListener {

    /**
     * Callback for Item clicked
     *
     * @param movie clicked movie item object
     */
    fun onItemClicked(movie: Movie?)
}