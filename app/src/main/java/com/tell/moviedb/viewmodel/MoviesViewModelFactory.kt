package com.tell.moviedb.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tell.moviedb.data.repo.MoviesRepository

class MoviesViewModelFactory (
    private val repository: MoviesRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MoviesViewModel(
            repository
        ) as T
    }
}
