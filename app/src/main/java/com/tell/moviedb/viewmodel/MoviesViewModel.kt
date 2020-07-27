package com.tell.moviedb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.tell.moviedb.data.model.MovieDetail
import com.tell.moviedb.data.model.PopularMovieResponse
import com.tell.moviedb.data.repo.MoviesRepository
import com.tell.moviedb.network.RetrofitResult
import io.reactivex.Maybe
import io.reactivex.disposables.Disposable
import java.util.*


class MoviesViewModel internal constructor(
    repository: MoviesRepository
) : ViewModel() {

    private val queryLiveData = MutableLiveData<Search>()

    private val detailLiveData = MutableLiveData<Detail>()


    val popularMoviesRepoResult: LiveData<RetrofitResult<PopularMovieResponse>> = queryLiveData.switchMap { param ->
        val maybe = repository.getPopularMovieResultStream(
            param.language, param.page, param.region
        )
        maybe.asLiveData()
    }

    val detailRepoResult: LiveData<RetrofitResult<MovieDetail>> = detailLiveData.switchMap { param ->
        val maybe = repository.getMovieDetailResultStream(
            param.movieId, param.language, param.appendToResponse
        )
        maybe.asLiveData()
    }

    fun searchPopularMovies(
        language: String = Locale.getDefault().language,
        page: Int = 1,
        region:  String? = null
    ) {
        queryLiveData.postValue(Search(language, page, region))
    }

    fun movieDetail(
        movieId: Int,
        language: String = Locale.getDefault().language,
        appendToResponse:  String? = null
    ) {
        detailLiveData.postValue(Detail(movieId, language, appendToResponse))
    }

    private fun <T> Maybe<T>.asLiveData(): LiveData<RetrofitResult<T>> {
        return MaybeLiveData(this)
    }

    internal data class Search(
        val language: String,
        val page: Int,
        val region:  String?
    )

    internal data class Detail(
        val movieId: Int,
        val language: String,
        val appendToResponse:  String?
    )
}


