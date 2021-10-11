package com.kenruizinoue.umbachallenge.di

import android.app.Activity
import android.content.Context
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.kenruizinoue.umbachallenge.contract.MainContract
import com.kenruizinoue.umbachallenge.model.MovieRepository
import com.kenruizinoue.umbachallenge.model.local.MovieDatabase
import com.kenruizinoue.umbachallenge.model.network.ApiService
import com.kenruizinoue.umbachallenge.model.network.ServiceBuilder
import com.kenruizinoue.umbachallenge.view.MainActivity
import com.kenruizinoue.umbachallenge.view.MovieAdapter
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope

object MainModule {

    @Module
    @InstallIn(ActivityComponent::class)
    abstract class MainContractModule {
        @Binds
        abstract fun bindView(activity: MainActivity): MainContract.View
    }

    @Module
    @InstallIn(ActivityComponent::class)
    object MainActivityModule {
        @Provides
        fun provideApiService(): ApiService {
            return ServiceBuilder.create()
        }

        @Provides
        fun provideMovieRepository(
            @ApplicationContext appContext: Context,
            apiService: ApiService
        ): MovieRepository {
            return MovieRepository(
                movieDao = Room.databaseBuilder(
                    appContext,
                    MovieDatabase::class.java,
                    "db"
                )
                    .build()
                    .getMovieDao(),
                apiService = apiService
            )
        }

        @Provides
        fun provideMainActivity(activity: Activity): MainActivity {
            return activity as MainActivity
        }

        @Provides
        fun provideCoroutineScope(activity: MainActivity): CoroutineScope {
            return activity.lifecycleScope
        }

        @Provides
        fun provideAdapter(): MovieAdapter {
            return MovieAdapter(arrayListOf())
        }
    }
}