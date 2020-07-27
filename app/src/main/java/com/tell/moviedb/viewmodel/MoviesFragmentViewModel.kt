package com.tell.moviedb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.tell.moviedb.data.DataSourceState
import com.tell.moviedb.data.MovieDataSourceFactory
import com.tell.moviedb.data.model.Movie
import io.reactivex.disposables.CompositeDisposable

class MoviesFragmentViewModel: ViewModel() {

    private val stateDataSourceLiveData = MutableLiveData<DataSourceState>()

    private val compositeDisposable = CompositeDisposable()


    val pageListLiveData: LiveData<PagedList<Movie>> by lazy {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(1 shl 3)
            .build()

        val factory = MovieDataSourceFactory(compositeDisposable, stateDataSourceLiveData)

        LivePagedListBuilder(factory, pagedListConfig)
            .build()
    }

    val stateDataSource:LiveData<DataSourceState> = stateDataSourceLiveData

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()
    }
}