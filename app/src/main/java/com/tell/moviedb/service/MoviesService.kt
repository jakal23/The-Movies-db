package com.tell.moviedb.service

import com.tell.moviedb.data.model.MovieDetail
import com.tell.moviedb.data.model.PopularMovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface MoviesService {

    @GET("/3/movie/popular")
    fun popularMovies(
        @Query("language") language: String = Locale.getDefault().language,
        @Query("page") page: Int = 1,
        @Query("region") region: String? = null
    ) : Single<PopularMovieResponse>

    @GET("/3/movie/{movie_id}")
    fun movieDetail(
        @Path(value = "movie_id", encoded = true) id: Int,
        @Query("language") language: String = Locale.getDefault().language,
        @Query("append_to_response") appendToResponse: String? = null
    ) : Single<MovieDetail>
}
