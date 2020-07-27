package com.tell.moviedb.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.tell.moviedb.data.model.Movie
import com.tell.moviedb.data.repo.MoviesRepository
import com.tell.moviedb.network.NetworkModule
import com.tell.moviedb.service.MoviesService
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val stateLiveData: MutableLiveData<DataSourceState>
): DataSource.Factory<Int, Movie>() {

    private val dataSource: MovieDataSource by lazy {
        MovieDataSource(repo(), compositeDisposable, stateLiveData)
    }

    override fun create(): DataSource<Int, Movie> {
        return dataSource
    }

    private fun repo(): MoviesRepository {
        val service = NetworkModule
            .retrofit
            .create(MoviesService::class.java)

        return MoviesRepository(service)
    }
}