package com.kenruizinoue.umbachallenge.presenter

import com.kenruizinoue.umbachallenge.contract.MainContract
import com.kenruizinoue.umbachallenge.model.Movie
import com.kenruizinoue.umbachallenge.model.MovieRepository
import com.kenruizinoue.umbachallenge.util.Constants.TYPE_LATEST
import com.kenruizinoue.umbachallenge.util.Constants.TYPE_POPULAR
import com.kenruizinoue.umbachallenge.util.TimestampUtils.getMinutesDifference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class MainPresenter
@Inject constructor(
    private val scope: CoroutineScope,
    private val mainView: MainContract.View,
    private val movieRepository: MovieRepository,
    @Named("io") private val ioDispatcher: CoroutineDispatcher,
    @Named("main") private val mainDispatcher: CoroutineDispatcher
) : MainContract.Presenter {

    var selectedType: String = TYPE_POPULAR
    var refreshingData: Boolean = false

    override fun onLoadData(type: String) {
        selectedType = type
        scope.launch(ioDispatcher) {
            val movies = movieRepository.getLocalMovies(type)

            when {
                movies.isEmpty() && type != TYPE_LATEST -> {
                    // DB empty, fetch remote data
                    withContext(mainDispatcher) { mainView.showRefreshAnimation() }
                    fetchRemoteData(type)
                }
                movies.isEmpty() && type == TYPE_LATEST -> {
                    // DB empty & latest option selected, fetch remote latest movie
                    withContext(mainDispatcher) { mainView.showRefreshAnimation() }
                    fetchRemoteLatestMovie()
                }
                movies.isNotEmpty() && type != TYPE_LATEST && isDataOld(movies[0]) -> {
                    // DB not empty & data is old, fetch remote data
                    withContext(mainDispatcher) { mainView.showRefreshAnimation() }
                    fetchRemoteData(type)
                }
                movies.size == 1 && type == TYPE_LATEST && isDataOld(movies[0]) -> {
                    // DB not empty & latest option selected & data is old, fetch remote latest movie
                    withContext(mainDispatcher) { mainView.showRefreshAnimation() }
                    fetchRemoteLatestMovie()
                }
                movies.isNotEmpty() && type != TYPE_LATEST && !isDataOld(movies[0]) -> {
                    // DB not empty & data is not old, show current data
                    withContext(mainDispatcher) { mainView.displayData(movies) }
                }
                movies.size == 1 && type == TYPE_LATEST && !isDataOld(movies[0]) -> {
                    // DB not empty & latest option selected & data is not old, show current data
                    withContext(mainDispatcher) { mainView.displayData(movies) }
                }
                else -> {
                    mainView.showSnackbar()
                }
            }
        }
    }

    override fun onRefreshData() {
        refreshingData = true
        scope.launch(ioDispatcher) {
            withContext(mainDispatcher) { mainView.showRefreshAnimation() }
            if (selectedType != TYPE_LATEST) {
                fetchRemoteData(selectedType)
            } else {
                fetchRemoteLatestMovie()
            }
        }
    }

    private suspend fun fetchRemoteData(type: String) {
        val response = movieRepository.getRemoteMovies(type)
        if (response.isSuccessful) {
            movieRepository.deleteLocalMovies(type)
            response.body()?.let {
                it.results.map { movie ->
                    movie.type = type
                    movie.inserted_time = System.currentTimeMillis()
                }
                movieRepository.insertLocalMovies(it.results)
            }
            withContext(mainDispatcher) {
                if (refreshingData) {
                    refreshingData = false
                    mainView.showToast("Data updated")
                }
                response.body()?.let {
                    mainView.displayData(it.results)
                }
            }
        } else {
            mainView.showSnackbar()
        }
    }

    private suspend fun fetchRemoteLatestMovie() {
        val response = movieRepository.getRemoteLatestMovie()
        if (response.isSuccessful) {
            movieRepository.deleteLocalMovies(TYPE_LATEST)
            response.body()?.let {
                it.type = TYPE_LATEST
                it.inserted_time = System.currentTimeMillis()
                movieRepository.insertLocalMovies(listOf(it))
            }
            withContext(mainDispatcher) {
                if (refreshingData) {
                    refreshingData = false
                    mainView.showToast("Data updated")
                }
                response.body()?.let {
                    mainView.displayData(listOf(it))
                }
            }
        } else {
            mainView.showSnackbar()
        }
    }

    // if the data has more than 10 minutes in the database, it's old
    private fun isDataOld(movie: Movie): Boolean =
        getMinutesDifference(movie.inserted_time, System.currentTimeMillis()) > 10
}