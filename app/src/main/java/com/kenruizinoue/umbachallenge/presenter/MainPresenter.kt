package com.kenruizinoue.umbachallenge.presenter

import com.kenruizinoue.umbachallenge.contract.MainContract
import com.kenruizinoue.umbachallenge.model.MovieRepository
import com.kenruizinoue.umbachallenge.util.Constants.TYPE_LATEST
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class MainPresenter
@Inject constructor(
    private val scope: CoroutineScope,
    private val mainView: MainContract.View,
    private val movieRepository: MovieRepository
) : MainContract.Presenter {

    override fun onFetchStart(type: String) {
        scope.launch(Dispatchers.IO) {
            movieRepository.getLocalMovies(type).collectLatest { movies ->
                when {
                    movies.isEmpty() && type != TYPE_LATEST -> {
                        // DB empty
                        fetchRemoteData(type)
                    }
                    movies.isEmpty() && type == TYPE_LATEST -> {
                        // DB empty with latest
                        fetchRemoteMovie(type)
                    }
                    movies.size == 1 && type == TYPE_LATEST -> {
                        // DB not empty with latest
                        mainView.displayMovie(movies[0])
                    }
                    else -> {
                        // DB not empty
                        withContext(Dispatchers.Main) {
                            mainView.displayData(movies)
                        }
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

    private suspend fun fetchRemoteData(type: String) {
        val response = movieRepository.getRemoteMovies(type)
        if (response.isSuccessful) {
            withContext(Dispatchers.Main) {
                response.body()?.let { mainView.displayData(it.results) }
            }
        } else {
            // todo handle exception
            throw IOException(response.message())
        }
    }

    private fun fetchRemoteMovie(type: String) {
        // todo implement
    }

}