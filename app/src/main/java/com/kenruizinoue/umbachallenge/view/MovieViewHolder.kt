package com.kenruizinoue.umbachallenge.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kenruizinoue.umbachallenge.R
import com.kenruizinoue.umbachallenge.model.Movie

class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val movieTitle: TextView = itemView.findViewById(R.id.movieTitle)
    private val releaseDateText: TextView = itemView.findViewById(R.id.releaseDateText)
    private val voteText: TextView = itemView.findViewById(R.id.voteText)
    private val movieImage: ImageView = itemView.findViewById(R.id.movieImage)
    lateinit var movie: Movie

    init {
        itemView.setOnClickListener(this)
    }

    fun bind(movie: Movie) {
        this.movie = movie

        Glide.with(itemView.context)
            .load("https://image.tmdb.org/t/p/w500${movie.backdrop_path}")
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(RequestOptions().placeholder(R.drawable.mock_image))
            .into(movieImage)
        movieTitle.text = movie.title
        releaseDateText.text = movie.release_date
        voteText.text = movie.vote_average
    }

    override fun onClick(view: View) {

    }
}