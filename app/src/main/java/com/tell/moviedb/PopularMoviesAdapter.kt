package com.tell.moviedb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tell.moviedb.data.model.Movie
import com.tell.moviedb.network.Constants.POSTER_BASE_URL
import kotlinx.android.synthetic.main.movie_list_item.view.*

class PopularMoviesAdapter(
    diffCallback: DiffUtil.ItemCallback<Movie>,
    listener: OnAdapterListSelectedListener
): PagedListAdapter<Movie, RecyclerView.ViewHolder>(diffCallback) {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            @Suppress("UNCHECKED_CAST")
            val item = v.tag as Movie

            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            listener.onItemSelected(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType){
            MOVIE_VIEW_TYPE -> MovieViewHolder(mOnClickListener, inflater.inflate(R.layout.movie_list_item, parent, false))
            EXTRA_VIEW_TYPE -> ExtraViewHolder(inflater.inflate(R.layout.extra_list_item, parent, false))
            else -> throw IllegalStateException("$viewType not implemented")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return when(val viewType = getItemViewType(position)){
            MOVIE_VIEW_TYPE -> (holder as MovieViewHolder).bind(getItem(position))
            EXTRA_VIEW_TYPE -> (holder as ExtraViewHolder).bind()
            else -> throw IllegalStateException("$viewType not implemented")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLastPosition(position))
            EXTRA_VIEW_TYPE
        else
            MOVIE_VIEW_TYPE
    }

    private fun isLastPosition(currentPosition: Int): Boolean{
        return currentPosition == itemCount - 1
    }


    internal class MovieViewHolder(
        private val listener: View.OnClickListener,
        itemView: View
    ): RecyclerView.ViewHolder(itemView) {

        fun bind(movie: Movie?) {
            itemView.cv_movie_title.text = movie?.title
            itemView.cv_movie_release_date.text =  movie?.releaseDate

            val moviePosterURL = POSTER_BASE_URL + movie?.posterPath

            Glide.with(itemView.context)
                .load(moviePosterURL)
                .into(itemView.cv_iv_movie_poster);

            with(itemView) {
                tag = movie
                setOnClickListener(listener)
            }
        }
    }

    internal class ExtraViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(){

        }
    }

    interface OnAdapterListSelectedListener {
        fun onItemSelected(movie: Movie)
    }

    companion object{
        const val MOVIE_VIEW_TYPE = 1
        const val EXTRA_VIEW_TYPE = 2
    }
}