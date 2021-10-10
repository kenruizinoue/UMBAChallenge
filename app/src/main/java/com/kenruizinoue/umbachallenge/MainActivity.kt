package com.kenruizinoue.umbachallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.kenruizinoue.umbachallenge.main.MainContract
import com.kenruizinoue.umbachallenge.model.Movie
import com.kenruizinoue.umbachallenge.model.MovieRepository
import com.kenruizinoue.umbachallenge.model.local.MovieDatabase
import com.kenruizinoue.umbachallenge.presenter.MainPresenter

class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var textView: TextView
    private lateinit var presenter: MainPresenter
    private lateinit var repository: MovieRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textView)
        // todo inject db with Hilt
        repository = MovieRepository(Room.databaseBuilder(this, MovieDatabase::class.java, "db").build().getMovieDao())
        presenter = MainPresenter(lifecycleScope, this, repository)
        presenter.onFetchStart("latest")
    }

    override fun displayData(movies: List<Movie>) {
        textView.text = movies.size.toString()
    }

    override fun displayMovie(movie: Movie) {
        TODO("Not yet implemented")
    }

    override fun showToast(message: String) {
        TODO("Not yet implemented")
    }

    override fun stopRefreshAnimation() {
        TODO("Not yet implemented")
    }

    override fun showSnackbar() {
        TODO("Not yet implemented")
    }
}