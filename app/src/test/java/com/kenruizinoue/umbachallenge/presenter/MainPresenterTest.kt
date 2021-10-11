package com.kenruizinoue.umbachallenge.presenter

import com.kenruizinoue.umbachallenge.contract.MainContract
import com.kenruizinoue.umbachallenge.model.Movie
import com.kenruizinoue.umbachallenge.model.MovieRepository
import com.kenruizinoue.umbachallenge.model.network.Movies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest {
    private lateinit var presenter: MainPresenter
    private lateinit var movieRepository: MovieRepository
    private lateinit var view: MainContract.View
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var testIoDispatcher: TestCoroutineDispatcher
    private lateinit var testMainDispatcher: TestCoroutineDispatcher

    @Before
    fun setUp() {
        movieRepository = mock(MovieRepository::class.java)
        view = mock(MainContract.View::class.java)
        coroutineScope = mock(CoroutineScope::class.java)
        testIoDispatcher = TestCoroutineDispatcher()
        testMainDispatcher = TestCoroutineDispatcher()
        presenter = MainPresenter(
            scope = TestCoroutineScope(),
            mainView = view,
            movieRepository = movieRepository,
            ioDispatcher = testIoDispatcher,
            mainDispatcher = testMainDispatcher
        )
    }

    @Test
    fun `when onLoadData() called with a type, then getLocalMovies() should be called with the same type`() =
        runBlockingTest {
            // when
            presenter.onLoadData("popular")

            // then
            Mockito.verify(movieRepository).getLocalMovies("popular")
        }

    @Test
    fun `given empty db, when onLoadData() called, then getRemoteMovies() should be called`() =
        runBlockingTest {
            // given
            val response = Response.success(Movies(newPopularMovies))
            Mockito.`when`(movieRepository.getLocalMovies("popular")).thenReturn(emptyList())
            Mockito.`when`(movieRepository.getRemoteMovies("popular")).thenReturn(response)

            // when
            presenter.onLoadData("popular")
            Mockito.verify(movieRepository).getLocalMovies("popular")
            Mockito.verify(view).showRefreshAnimation()

            // then
            Mockito.verify(movieRepository).getRemoteMovies("popular")
            Mockito.verify(movieRepository).deleteLocalMovies("popular")
            Mockito.verify(movieRepository).insertLocalMovies(newPopularMovies)
            Mockito.verify(view).displayData(newPopularMovies)
        }

    @Test
    fun `given empty db, when onLoadData() called with latest type, then getRemoteLatestMovie() should be called`() =
        runBlockingTest {
            // given
            val response = Response.success(newLatestMovie)
            Mockito.`when`(movieRepository.getLocalMovies("latest")).thenReturn(emptyList())
            Mockito.`when`(movieRepository.getRemoteLatestMovie()).thenReturn(response)

            // when
            presenter.onLoadData("latest")
            Mockito.verify(movieRepository).getLocalMovies("latest")
            Mockito.verify(view).showRefreshAnimation()

            // then
            Mockito.verify(movieRepository).getRemoteLatestMovie()
            Mockito.verify(movieRepository).deleteLocalMovies("latest")
            Mockito.verify(movieRepository).insertLocalMovies(listOf(newLatestMovie))
            Mockito.verify(view).displayData(listOf(newLatestMovie))
        }

    @Test
    fun `given old data in db, when onLoadData() called, then getRemoteMovies() should be called`() =
        runBlockingTest {
            // given
            val response = Response.success(Movies(newPopularMovies))
            Mockito.`when`(movieRepository.getLocalMovies("popular")).thenReturn(oldPopularMovies)
            Mockito.`when`(movieRepository.getRemoteMovies("popular")).thenReturn(response)

            // when
            presenter.onLoadData("popular")
            Mockito.verify(movieRepository).getLocalMovies("popular")
            Mockito.verify(view).showRefreshAnimation()

            // then
            Mockito.verify(movieRepository).getRemoteMovies("popular")
            Mockito.verify(movieRepository).deleteLocalMovies("popular")
            Mockito.verify(movieRepository).insertLocalMovies(newPopularMovies)
            Mockito.verify(view).displayData(newPopularMovies)
        }

    @Test
    fun `given old data in db, when onLoadData() called with latest, then getRemoteLatestMovie() should be called`() =
        runBlockingTest {
            // given
            val response = Response.success(newLatestMovie)
            Mockito.`when`(movieRepository.getLocalMovies("latest")).thenReturn(listOf(oldLatestMovie))
            Mockito.`when`(movieRepository.getRemoteLatestMovie()).thenReturn(response)

            // when
            presenter.onLoadData("latest")
            Mockito.verify(movieRepository).getLocalMovies("latest")
            Mockito.verify(view).showRefreshAnimation()

            // then
            Mockito.verify(movieRepository).getRemoteLatestMovie()
            Mockito.verify(movieRepository).deleteLocalMovies("latest")
            Mockito.verify(movieRepository).insertLocalMovies(listOf(newLatestMovie))
            Mockito.verify(view).displayData(listOf(newLatestMovie))
        }

    @Test
    fun `given new data in db, when onLoadData() called, then displayData() should be called immediately`() =
        runBlockingTest {
            // given
            Mockito.`when`(movieRepository.getLocalMovies("popular")).thenReturn(newPopularMovies)

            // when
            presenter.onLoadData("popular")

            // then
            Mockito.verify(movieRepository).getLocalMovies("popular")
            Mockito.verify(view).displayData(newPopularMovies)
        }

    @Test
    fun `given new data in db, when onLoadData() called with latest, then displayData() should be called immediately`() =
        runBlockingTest {
            // given
            Mockito.`when`(movieRepository.getLocalMovies("latest")).thenReturn(listOf(newLatestMovie))

            // when
            presenter.onLoadData("latest")

            // then
            Mockito.verify(movieRepository).getLocalMovies("latest")
            Mockito.verify(view).displayData(listOf(newLatestMovie))
        }

    // refresh
    @Test
    fun `when onRefreshData() called for the first time, then getRemoteMovies() should be called with popular`() =
        runBlockingTest {
            // when
            val response = Response.success(Movies(newPopularMovies))
            Mockito.`when`(movieRepository.getRemoteMovies("popular")).thenReturn(response)
            presenter.onRefreshData()

            // then
            Mockito.verify(movieRepository).getRemoteMovies("popular")
            Mockito.verify(movieRepository).deleteLocalMovies("popular")
            Mockito.verify(movieRepository).insertLocalMovies(newPopularMovies)
            Mockito.verify(view).showToast("Data updated")
            Mockito.verify(view).displayData(newPopularMovies)
        }

    @Test
    fun `when onRefreshData() after fetched data for upcoming, then getRemoteMovies() should be called with upcoming`() =
        runBlockingTest {
            // when
            val response = Response.success(Movies(newRemoteUpcomingMovies))
            Mockito.`when`(movieRepository.getRemoteMovies("upcoming")).thenReturn(response)
            Mockito.`when`(movieRepository.getLocalMovies("upcoming")).thenReturn(newUpcomingMovies)
            presenter.onLoadData("upcoming")
            Mockito.verify(view).displayData(newUpcomingMovies)
            presenter.onRefreshData()

            // then
            Mockito.verify(movieRepository).getRemoteMovies("upcoming")
            Mockito.verify(movieRepository).deleteLocalMovies("upcoming")
            Mockito.verify(movieRepository).insertLocalMovies(newRemoteUpcomingMovies)
            Mockito.verify(view).showToast("Data updated")
            Mockito.verify(view).displayData(newRemoteUpcomingMovies)
        }

    @Test
    fun `when onRefreshData() after fetched data for latest, then getRemoteLatestMovie() should be called`() =
        runBlockingTest {
            // when
            val response = Response.success(newRemoteLatestMovie)
            Mockito.`when`(movieRepository.getRemoteLatestMovie()).thenReturn(response)
            Mockito.`when`(movieRepository.getLocalMovies("latest")).thenReturn(listOf(newLatestMovie))
            presenter.onLoadData("latest")
            Mockito.verify(view).displayData(listOf(newLatestMovie))
            presenter.onRefreshData()

            // then
            Mockito.verify(movieRepository).getRemoteLatestMovie()
            Mockito.verify(movieRepository).deleteLocalMovies("latest")
            Mockito.verify(movieRepository).insertLocalMovies(listOf(newRemoteLatestMovie))
            Mockito.verify(view).showToast("Data updated")
            Mockito.verify(view).displayData(listOf(newRemoteLatestMovie))
        }

    @Test
    fun `given empty db with error network response, when onLoadData() called, then getRemoteMovies() and showSnackbar() should be called`() =
        runBlockingTest {
            // given
            val errorResponse = Response.error<Movies>(
                404,
                ResponseBody.create(
                    MediaType.parse("application/json"), "{}"
                )
            )
            Mockito.`when`(movieRepository.getRemoteMovies("popular")).thenReturn(errorResponse)
            Mockito.`when`(movieRepository.getLocalMovies("popular")).thenReturn(emptyList())

            // when
            presenter.onLoadData("popular")

            // then
            Mockito.verify(view).showRefreshAnimation()
            Mockito.verify(movieRepository).getRemoteMovies("popular")
            Mockito.verify(view).showSnackbar()
        }

    @Test
    fun `given empty db with error network response, when onLoadData() called with latest, then getRemoteLatestMovie() and showSnackbar() should be called`() =
        runBlockingTest {
            // given
            val errorResponse = Response.error<Movie>(
                404,
                ResponseBody.create(
                    MediaType.parse("application/json"), "{}"
                )
            )
            Mockito.`when`(movieRepository.getRemoteLatestMovie()).thenReturn(errorResponse)
            Mockito.`when`(movieRepository.getLocalMovies("latest")).thenReturn(emptyList())

            // when
            presenter.onLoadData("latest")

            // then
            Mockito.verify(view).showRefreshAnimation()
            Mockito.verify(movieRepository).getRemoteLatestMovie()
            Mockito.verify(view).showSnackbar()
        }

    private val oldPopularMovies = listOf(
        Movie(
            dbId = 1,
            id = 111,
            backdrop_path = "URL 1",
            title = "Title 1",
            overview = "Description 1",
            vote_average = "1",
            release_date = "2020-01-01",
            type = "popular",
            inserted_time = 0
        ),
        Movie(
            dbId = 2,
            id = 222,
            backdrop_path = "URL 2",
            title = "Title 2",
            overview = "Description 2",
            vote_average = "2",
            release_date = "2020-02-02",
            type = "popular",
            inserted_time = 0
        )
    )

    private val newPopularMovies = listOf(
        Movie(
            dbId = 1,
            id = 111,
            backdrop_path = "URL 1",
            title = "Title 1",
            overview = "Description 1",
            vote_average = "1",
            release_date = "2020-01-01",
            type = "popular",
            inserted_time = System.currentTimeMillis()
        ),
        Movie(
            dbId = 2,
            id = 222,
            backdrop_path = "URL 2",
            title = "Title 2",
            overview = "Description 2",
            vote_average = "2",
            release_date = "2020-02-02",
            type = "popular",
            inserted_time = System.currentTimeMillis()
        )
    )

    private val newUpcomingMovies = listOf(
        Movie(
            dbId = 1,
            id = 111,
            backdrop_path = "URL 1",
            title = "Title 1",
            overview = "Description 1",
            vote_average = "1",
            release_date = "2020-01-01",
            type = "upcoming",
            inserted_time = System.currentTimeMillis()
        ),
        Movie(
            dbId = 2,
            id = 222,
            backdrop_path = "URL 2",
            title = "Title 2",
            overview = "Description 2",
            vote_average = "2",
            release_date = "2020-02-02",
            type = "upcoming",
            inserted_time = System.currentTimeMillis()
        )
    )

    private val newRemoteUpcomingMovies = listOf(
        Movie(
            dbId = 1,
            id = 111,
            backdrop_path = "URL 1",
            title = "Title 1",
            overview = "Description 1",
            vote_average = "1",
            release_date = "2020-01-01",
            type = "upcoming",
            inserted_time = System.currentTimeMillis() + 1
        ),
        Movie(
            dbId = 2,
            id = 222,
            backdrop_path = "URL 2",
            title = "Title 2",
            overview = "Description 2",
            vote_average = "2",
            release_date = "2020-02-02",
            type = "upcoming",
            inserted_time = System.currentTimeMillis() + 1
        )
    )

    private val oldLatestMovie = Movie(
        dbId = 1,
        id = 111,
        backdrop_path = "URL 1",
        title = "Title 1",
        overview = "Description 1",
        vote_average = "1",
        release_date = "2020-01-01",
        type = "latest",
        inserted_time = 0
    )

    private val newLatestMovie = Movie(
        dbId = 1,
        id = 111,
        backdrop_path = "URL 1",
        title = "Title 1",
        overview = "Description 1",
        vote_average = "1",
        release_date = "2020-01-01",
        type = "latest",
        inserted_time = System.currentTimeMillis()
    )

    private val newRemoteLatestMovie = Movie(
        dbId = 1,
        id = 111,
        backdrop_path = "URL 1",
        title = "Title 1",
        overview = "Description 1",
        vote_average = "1",
        release_date = "2020-01-01",
        type = "latest",
        inserted_time = System.currentTimeMillis() + 1
    )
}