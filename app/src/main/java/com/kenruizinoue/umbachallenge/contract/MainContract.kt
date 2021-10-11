package com.kenruizinoue.umbachallenge.contract

import com.kenruizinoue.umbachallenge.model.Movie

class MainContract {
    interface View {
        fun displayData(movies: List<Movie>)
        fun displayMovie(movie: Movie)
        fun showToast(message: String)
        fun stopRefreshAnimation()
        fun showSnackbar()
    }

    interface Presenter {
        fun onLoadData(type: String)
        fun onFetchLatestMovie(useRemoteSource: Boolean)
        fun onRefreshData(type: String)
    }
}