package com.kenruizinoue.umbachallenge.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
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

    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @Inject
    lateinit var presenter: MainPresenter

    @Inject
    lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        constraintLayout = findViewById(R.id.containerLayout)
        recyclerView = findViewById(R.id.recyclerView)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        swipeRefreshLayout = findViewById(R.id.swipeRefresh)
        recyclerView.apply { adapter = movieAdapter }
        setupBottomNavListener()
        swipeRefreshLayout.setOnRefreshListener {
            presenter.onRefreshData()
        }
        presenter.onLoadData(TYPE_POPULAR)
    }

    override fun displayData(movies: List<Movie>) {
        swipeRefreshLayout.isRefreshing = false
        movieAdapter.updateData(movies)
        recyclerView.layoutManager?.scrollToPosition(0)
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showRefreshAnimation() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun showSnackbar() {
        Snackbar.make(
            constraintLayout,
            getString(R.string.snackbar_message),
            Snackbar.LENGTH_LONG
        )
            .setAction(getString(R.string.try_again)) {
                presenter.onRefreshData()
            }.show()
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
                    showSnackbar()
                }
            }
            true
        }
    }
}