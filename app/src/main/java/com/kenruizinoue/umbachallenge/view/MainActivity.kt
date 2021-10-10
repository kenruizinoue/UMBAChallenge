package com.kenruizinoue.umbachallenge.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.kenruizinoue.umbachallenge.MainContract
import com.kenruizinoue.umbachallenge.R
import com.kenruizinoue.umbachallenge.model.Movie
import com.kenruizinoue.umbachallenge.model.MovieRepository
import com.kenruizinoue.umbachallenge.model.local.MovieDatabase
import com.kenruizinoue.umbachallenge.model.network.ServiceBuilder
import com.kenruizinoue.umbachallenge.presenter.MainPresenter
import com.kenruizinoue.umbachallenge.util.Constants.TYPE_POPULAR

class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var presenter: MainPresenter
    private lateinit var repository: MovieRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        // todo inject db with Hilt
        repository = MovieRepository(
            movieDao = Room.databaseBuilder(this, MovieDatabase::class.java, "db").build().getMovieDao(),
            apiService = ServiceBuilder.create()
        )
        presenter = MainPresenter(lifecycleScope, this, repository)
        presenter.onFetchStart(TYPE_POPULAR)

        movieAdapter = MovieAdapter(arrayListOf())
        recyclerView.apply { adapter = movieAdapter }
    }

    override fun displayData(movies: List<Movie>) {
        movieAdapter.updateData(movies)
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