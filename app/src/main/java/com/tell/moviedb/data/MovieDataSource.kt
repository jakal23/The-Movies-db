package com.tell.moviedb.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.tell.moviedb.data.model.Movie
import com.tell.moviedb.data.model.PopularMovieResponse
import com.tell.moviedb.data.repo.MoviesRepository
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class MovieDataSource(
    private val repository: MoviesRepository,
    private val compositeDisposable: CompositeDisposable,
    private val stateLiveData: MutableLiveData<DataSourceState>
): PageKeyedDataSource<Int, Movie>() {

    private var startPage = 1

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        Log.d(TAG, "loadInitial, placeholdersEnabled = " + params.placeholdersEnabled +
                ", requestedLoadSize = " + params.requestedLoadSize)

        getPopularMovieResultStream(startPage) {
            callback.onResult(it.results, null, startPage + 1)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        Log.d(TAG, "loadAfter, key = " + params.key +
                ", requestedLoadSize = " + params.requestedLoadSize)

        getPopularMovieResultStream(params.key) {
            if(it.totalPages >= params.key)
                callback.onResult(it.results, params.key + 1)
            else
                stateLiveData.postValue(DataSourceState.COMPLETE)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        Log.d(TAG, "loadBefore, key = " + params.key +
                ", requestedLoadSize = " + params.requestedLoadSize)
    }

    private fun getPopularMovieResultStream(page: Int, onSuccess:(PopularMovieResponse) -> Unit){
        stateLiveData.postValue(DataSourceState.LOADING)

        compositeDisposable.add(
            repository.getPopularMovieResultStream(Locale.ROOT.language, page, null)
                .subscribe({
                    stateLiveData.postValue(DataSourceState.LOADED)
                    onSuccess(it)
                }, {
                    stateLiveData.postValue(DataSourceState.ERROR)
                })
        )
    }

    companion object{
        val TAG = MovieDataSource::class.java.simpleName
    }
}