package com.tell.moviedb.util

import androidx.recyclerview.widget.DiffUtil
import com.tell.moviedb.data.model.Movie

class MovieDifferCallback: DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}