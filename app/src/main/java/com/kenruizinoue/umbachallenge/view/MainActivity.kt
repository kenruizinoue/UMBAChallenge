package com.kenruizinoue.umbachallenge.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.kenruizinoue.umbachallenge.contract.MainContract
import com.kenruizinoue.umbachallenge.R
import com.kenruizinoue.umbachallenge.model.Movie
import com.kenruizinoue.umbachallenge.presenter.MainPresenter
import com.kenruizinoue.umbachallenge.util.Constants.TYPE_POPULAR
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var recyclerView: RecyclerView
    @Inject
    lateinit var presenter: MainPresenter
    @Inject
    lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        presenter.onFetchStart(TYPE_POPULAR)

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