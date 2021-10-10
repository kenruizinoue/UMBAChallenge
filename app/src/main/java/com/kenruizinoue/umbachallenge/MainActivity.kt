package com.kenruizinoue.umbachallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.kenruizinoue.umbachallenge.model.Movie
import com.kenruizinoue.umbachallenge.model.MovieRepository
import com.kenruizinoue.umbachallenge.model.local.MovieDatabase
import com.kenruizinoue.umbachallenge.model.network.ServiceBuilder
import com.kenruizinoue.umbachallenge.presenter.MainPresenter
import com.kenruizinoue.umbachallenge.util.Constants.TYPE_POPULAR

class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var textView: TextView
    private lateinit var presenter: MainPresenter
    private lateinit var repository: MovieRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textView)
        // todo inject db with Hilt
        repository = MovieRepository(
            movieDao = Room.databaseBuilder(this, MovieDatabase::class.java, "db").build().getMovieDao(),
            apiService = ServiceBuilder.create()
        )
        presenter = MainPresenter(lifecycleScope, this, repository)
        presenter.onFetchStart(TYPE_POPULAR)
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