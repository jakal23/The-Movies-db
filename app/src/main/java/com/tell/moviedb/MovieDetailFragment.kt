package com.tell.moviedb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.tell.moviedb.data.model.MovieDetail
import com.tell.moviedb.data.repo.MoviesRepository
import com.tell.moviedb.network.Constants.POSTER_BASE_URL
import com.tell.moviedb.network.NetworkModule
import com.tell.moviedb.network.RetrofitResult
import com.tell.moviedb.service.MoviesService
import com.tell.moviedb.viewmodel.MoviesViewModel
import com.tell.moviedb.viewmodel.MoviesViewModelFactory
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import java.text.NumberFormat
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class MovieDetailFragment : Fragment() {

    private lateinit var model: MoviesViewModel

    private val args: MovieDetailFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val service = NetworkModule
            .retrofit
            .create(MoviesService::class.java)

        model = ViewModelProvider(this, MoviesViewModelFactory(MoviesRepository(service)))
            .get(MoviesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progress_bar.visibility = View.VISIBLE
        txt_error.visibility = View.GONE

        val movieId = args.argMovieId

        model.detailRepoResult.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            progress_bar.visibility = View.GONE

            if (it is RetrofitResult.Success) {
                it.data?.run {
                    bind(this)
                }
            } else {
                txt_error.visibility = View.VISIBLE
            }
        })

        model.movieDetail(movieId, Locale.ROOT.language)
    }

    private fun bind(movie: MovieDetail) {
        movie_title.text = movie.title
        movie_tagline.text = movie.tagline
        movie_release_date.text = movie.releaseDate
        movie_runtime.setText(getString(R.string.minutes, movie.runtime))
        movie_overview.text = movie.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        movie_budget.text = formatCurrency.format(movie.budget)
        movie_revenue.text = formatCurrency.format(movie.revenue)

        movie.posterPath?.run {
            val moviePosterURL = POSTER_BASE_URL + this

            Glide.with(this@MovieDetailFragment)
                .load(moviePosterURL)
                .into(iv_movie_poster)
        }
    }

}