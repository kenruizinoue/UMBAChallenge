package com.kenruizinoue.umbachallenge.contract

import com.kenruizinoue.umbachallenge.model.Movie

class MainContract {
    interface View {
        fun displayData(movies: List<Movie>)
        fun showToast(message: String)
        fun showRefreshAnimation()
        fun showSnackbar()
    }

    interface Presenter {
        fun onLoadData(type: String)
        fun onRefreshData()
    }
}