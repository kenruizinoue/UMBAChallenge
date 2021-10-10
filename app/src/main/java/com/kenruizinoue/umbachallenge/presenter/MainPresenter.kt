package com.kenruizinoue.umbachallenge.presenter

import com.kenruizinoue.umbachallenge.main.MainContract
import com.kenruizinoue.umbachallenge.model.Movie
import com.kenruizinoue.umbachallenge.model.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainPresenter(val scope: CoroutineScope, val mainView: MainContract.View ,private val movieRepository: MovieRepository): MainContract.Presenter {
    override fun onFetchStart(type: String) {
        scope.launch(Dispatchers.IO) {
            movieRepository.getLocalMovies(type).collectLatest { list ->
                println(list.size)
                if (list.isEmpty()) {
                    val mockMovies = listOf(
                        Movie(
                            dbId = 1,
                            id = 1,
                            backdrop_path = null,
                            title = "Test title",
                            vote_average = "1",
                            overview = "Test overview",
                            release_date = "Test",
                            type = "latest"
                        ),
                        Movie(
                            dbId = 2,
                            id = 2,
                            backdrop_path = null,
                            title = "Test title 2",
                            vote_average = "2",
                            overview = "Test overview 2",
                            release_date = "Test",
                            type = "latest"
                        )
                    )
                    movieRepository.insertLocalMovies(mockMovies)
                    withContext(Dispatchers.Main) {
                        mainView.displayData(list)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        mainView.displayData(list)
                    }
                }
            }
        }
    }

    override fun onFetchLatestMovie(useRemoteSource: Boolean) {
        // todo implement
    }

    override fun onRefreshData(type: String) {
        // todo implement
    }

    override fun attach(view: MainContract.View) {
        // todo implement
    }

    override fun onDestroy() {
        // todo implement
    }

}