package com.kenruizinoue.umbachallenge.presenter

import com.kenruizinoue.umbachallenge.contract.MainContract
import com.kenruizinoue.umbachallenge.model.MovieRepository
import com.kenruizinoue.umbachallenge.util.Constants.TYPE_LATEST
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    override fun onLoadData(type: String) {
        scope.launch(Dispatchers.IO) {
            val movies = movieRepository.getLocalMovies(type)
            when {
                movies.isEmpty() && type != TYPE_LATEST -> {
                    // DB empty
                    fetchRemoteData(type)
                }
                movies.isEmpty() && type == TYPE_LATEST -> {
                    // DB empty & latest option selected
                    fetchRemoteLatestMovie()
                }
                movies.isNotEmpty() && type != TYPE_LATEST  -> {
                    // DB not empty
                    withContext(Dispatchers.Main) {
                        mainView.displayData(movies)
                    }
                }
                movies.size == 1 && type == TYPE_LATEST -> {
                    // DB not empty & latest option selected
                    withContext(Dispatchers.Main) {
                        mainView.displayData(movies)
                    }
                }
                else -> {
                    // todo handle exception
                }
            }
        }
    }

    override fun onRefreshData(type: String) {
        // todo implement
    }

    private suspend fun fetchRemoteData(type: String) {
        val response = movieRepository.getRemoteMovies(type)
        if (response.isSuccessful) {
            movieRepository.deleteLocalMovies(type)
            response.body()?.let {
                it.results.map { movie -> movie.type = type }
                movieRepository.insertLocalMovies(it.results)
            }
            withContext(Dispatchers.Main) {
                response.body()?.let { mainView.displayData(it.results) }
            }
        } else {
            // todo handle exception
            throw IOException(response.message())
        }
    }

    private suspend fun fetchRemoteLatestMovie() {
        val response = movieRepository.getRemoteLatestMovie()
        if (response.isSuccessful) {
            movieRepository.deleteLocalMovies(TYPE_LATEST)
            response.body()?.let {
                it.type = TYPE_LATEST
                movieRepository.insertLocalMovies(listOf(it))
            }
            withContext(Dispatchers.Main) {
                response.body()?.let {
                    mainView.displayData(listOf(it))
                }
            }
        } else {
            // todo handle exception
            throw IOException(response.message())
        }
    }

}