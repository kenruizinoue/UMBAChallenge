package com.kenruizinoue.umbachallenge.view

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kenruizinoue.umbachallenge.R
import com.kenruizinoue.umbachallenge.model.Movie
import com.kenruizinoue.umbachallenge.util.Constants.EXTRA_MOVIE

class DetailActivity : AppCompatActivity() {

    private lateinit var movieImage: ImageView
    private lateinit var voteText: TextView
    private lateinit var releaseDateText: TextView
    private lateinit var movieDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        movieImage = findViewById(R.id.movieImage)
        voteText = findViewById(R.id.voteText)
        releaseDateText = findViewById(R.id.releaseDateText)
        movieDescription = findViewById(R.id.movieDescription)

        val movie: Movie? = intent.getParcelableExtra(EXTRA_MOVIE)
        movie?.let {
            supportActionBar?.title = it.title
            if (it.backdrop_path != null)
                Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w500${it.backdrop_path}")
                    .into(movieImage)
            voteText.text = it.vote_average
            releaseDateText.text = verifyReleaseDateText(it.release_date)
            movieDescription.text = it.overview
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun verifyReleaseDateText(releaseDate: String?): String {
        if (releaseDate.isNullOrEmpty()) return "Without Release Date"
        return releaseDate
    }
}