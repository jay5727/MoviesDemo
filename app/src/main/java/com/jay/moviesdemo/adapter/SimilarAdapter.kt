package com.jay.moviesdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.jay.moviesdemo.databinding.ItemRowMovieBinding
import com.jay.moviesdemo.listener.ItemClickListener
import com.jay.moviesdemo.model.Movie
import com.jay.moviesdemo.viewmodel.MovieRowViewModel

/**
 * @param context object to access resources
 * @param moviesList list containing all information for similar movies
 */
class SimilarAdapter(
    private val context: Context,
    private val similarMoviesList: List<Movie>
) : RecyclerView.Adapter<SimilarAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        binding.btnBookNow.visibility = View.GONE
        binding.viewModel = MovieRowViewModel()
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return similarMoviesList.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(similarMoviesList[position])
    }

    /**
     * @param binding : binding file for Item Row Layout [ItemRowMovieBinding]
     */
    inner class ViewHolder(private val binding: ItemRowMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Movie) {
            binding.viewModel?.item?.set(item)
            binding.executePendingBindings()
        }
    }
}