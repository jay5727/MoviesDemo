package com.jay.moviesdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jay.moviesdemo.databinding.ItemCastBinding
import com.jay.moviesdemo.model.Cast

import com.jay.moviesdemo.viewmodel.CastRowViewModel

/**
 * @param context object to access resources
 * @param castList list containing all information of cast
 */
class CastAdapter(
    private val context: Context,
    private val castList: List<Cast>
) : RecyclerView.Adapter<CastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCastBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        binding.viewModel = CastRowViewModel()
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return castList.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(castList[position])
    }

    /**
     * @param binding : binding file for Item Row Layout [ItemCastBinding]
     */
    inner class ViewHolder(private val binding: ItemCastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Cast) {
            binding.viewModel?.item?.set(item)
            binding.executePendingBindings()
        }
    }
}