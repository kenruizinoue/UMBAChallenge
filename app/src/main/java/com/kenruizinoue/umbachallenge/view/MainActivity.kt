package com.kenruizinoue.umbachallenge.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kenruizinoue.umbachallenge.contract.MainContract
import com.kenruizinoue.umbachallenge.R
import com.kenruizinoue.umbachallenge.model.Movie
import com.kenruizinoue.umbachallenge.presenter.MainPresenter
import com.kenruizinoue.umbachallenge.util.Constants.TYPE_LATEST
import com.kenruizinoue.umbachallenge.util.Constants.TYPE_POPULAR
import com.kenruizinoue.umbachallenge.util.Constants.TYPE_TOP_RATED
import com.kenruizinoue.umbachallenge.util.Constants.TYPE_UPCOMING
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView
    @Inject
    lateinit var presenter: MainPresenter
    @Inject
    lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        recyclerView.apply { adapter = movieAdapter }
        setupBottomNavListener()
        presenter.onLoadData(TYPE_POPULAR)
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

    private fun setupBottomNavListener() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_popular -> {
                    presenter.onLoadData(TYPE_POPULAR)
                }
                R.id.action_upcoming -> {
                    presenter.onLoadData(TYPE_UPCOMING)
                }
                R.id.action_top_rated -> {
                    presenter.onLoadData(TYPE_TOP_RATED)
                }
                R.id.action_latest -> {
                    presenter.onLoadData(TYPE_LATEST)
                }
                else -> {
                    // todo handle exception
                }
            }
            true
        }
    }
}