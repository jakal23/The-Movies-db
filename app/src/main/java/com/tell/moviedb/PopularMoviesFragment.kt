package com.tell.moviedb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tell.moviedb.data.DataSourceState
import com.tell.moviedb.data.model.Movie
import com.tell.moviedb.util.MovieDifferCallback
import com.tell.moviedb.viewmodel.MoviesFragmentViewModel
import kotlinx.android.synthetic.main.fragment_popular_movies.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class PopularMoviesFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_popular_movies, container, false)

        val listView = view.findViewById<RecyclerView>(R.id.rv_movie_list)
        val movieAdapter = PopularMoviesAdapter(MovieDifferCallback(), object : PopularMoviesAdapter.OnAdapterListSelectedListener{
            override fun onItemSelected(movie: Movie) {
                navigateDetail(movie)
            }
        })

        with(listView){
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = movieAdapter
            setHasFixedSize(true)
        }

        val viewModel = moviesFragmentViewModel()
        viewModel.pageListLiveData.observe(viewLifecycleOwner, Observer {
            movieAdapter.submitList(it)
        })
        viewModel.stateDataSource.observe(viewLifecycleOwner, Observer {
            progress_bar_popular.visibility =
                if( movieAdapter.itemCount <= 0 && it == DataSourceState.LOADING) View.VISIBLE else View.GONE

            txt_error_popular.visibility =
                if( movieAdapter.itemCount <= 0 && it == DataSourceState.ERROR) View.VISIBLE else View.GONE
        })

        return view
    }

    private fun moviesFragmentViewModel(): MoviesFragmentViewModel {
        return ViewModelProvider(this)
            .get(MoviesFragmentViewModel::class.java)
    }

    private fun navigateDetail(movie: Movie) {
        val action = PopularMoviesFragmentDirections
            .actionPopularMoviesFragmentToSecondFragment(movie.id)
        findNavController().navigate(action)
    }

}