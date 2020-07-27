package com.tell.moviedb.data.repo

import android.os.Looper
import com.tell.moviedb.data.model.MovieDetail
import com.tell.moviedb.data.model.PopularMovieResponse
import com.tell.moviedb.service.MoviesService
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MoviesRepository(private val service: MoviesService) {


    fun getPopularMovieResultStream(language: String, page: Int, region:  String?): Maybe<PopularMovieResponse> {
        val single = requestPopularMovies(language, page, region)
        return single.filter {
            it.results.filter {movie ->
                !movie.adult
            }
            true
        }
    }

    fun getMovieDetailResultStream(id: Int, language: String, appendToResponse: String?): Maybe<MovieDetail> {
        val single = requestMovieDetail(id, language, appendToResponse)
        return single.filter {
            !it.adult
        }
    }

    private fun requestPopularMovies(language: String, page: Int, region:  String?): Single<PopularMovieResponse> {
        return service.popularMovies(language, page, region)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.from(Looper.getMainLooper()))
    }

    private fun requestMovieDetail(id: Int, language: String, appendToResponse: String?): Single<MovieDetail> {
        return service.movieDetail(id, language, appendToResponse)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.from(Looper.getMainLooper()))
    }
}