package com.jay.moviesdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jay.moviesdemo.databinding.ItemRowMovieBinding
import com.jay.moviesdemo.listener.ItemClickListener
import com.jay.moviesdemo.model.Movie
import com.jay.moviesdemo.viewmodel.MovieRowViewModel

/**
 * @param context object to access resources
 * @param moviesList list containing all information for movies
 */
class MoviesAdapter(
    private val context: Context,
    private val moviesList: List<Movie>
) : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    private var itemClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        binding.viewModel = MovieRowViewModel()
        binding.btnBookNow.setOnClickListener {
            binding.viewModel?.item?.get()?.let {
                itemClickListener?.onItemClicked(it)
            }

        }
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return moviesList.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(moviesList[position])
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

    /**
     * Sets [ItemClickListener] for listening movie item click.
     * @param clickListener Listener object
     */
    internal fun setOnItemClickListener(clickListener: ItemClickListener?) {
        itemClickListener = clickListener
    }
}