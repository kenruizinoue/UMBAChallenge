package com.kenruizinoue.umbachallenge.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kenruizinoue.umbachallenge.R
import com.kenruizinoue.umbachallenge.model.Movie

class MovieAdapter (private var movies: ArrayList<Movie>): RecyclerView.Adapter<MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        return holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<Movie>) {
        movies = ArrayList(newData)
        notifyDataSetChanged()
    }

}