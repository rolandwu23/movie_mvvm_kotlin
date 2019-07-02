package com.grok.akm.movie

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import butterknife.ButterKnife
import com.grok.akm.movie.Model.ApiResponseMovie
import com.grok.akm.movie.Model.pojo.Movie
import com.grok.akm.movie.Network.Status
import com.grok.akm.movie.Paging.HighestMovieAdapter
import com.grok.akm.movie.Utils.RxUtils
import com.grok.akm.movie.ViewModel.SearchViewModel
import com.grok.akm.movie.ViewModel.ViewModelFactory
import com.grok.akm.movie.di.MyApplication
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_search.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var searchViewModel: SearchViewModel? = null

    private var searchViewTextSubscription: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setToolbar()

        (application as MyApplication).appComponent!!.doInjection(this)
        ButterKnife.bind(this)

        val layoutManager = GridLayoutManager(this, 2)
        activity_search_recycler_view.layoutManager = layoutManager

        searchViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)

        searchViewModel?.getSearchMoviesLiveData()
            ?.observe(this, Observer<ApiResponseMovie> { this.consumeSearchMovieResponse(it) })
    }

    private fun setToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
    }

    private fun consumeSearchMovieResponse(apiResponse: ApiResponseMovie?) {

        when (apiResponse?.status) {

            Status.LOADING -> Snackbar.make(
                findViewById<View>(android.R.id.content),
                "Loading Movies...",
                Snackbar.LENGTH_SHORT
            )
                .show()

            Status.SUCCESS -> renderSuccessResponse(apiResponse.data?.movies)

            Status.ERROR -> Toast.makeText(this, resources.getString(R.string.errorString), Toast.LENGTH_SHORT).show()

            else -> {
            }
        }
    }

    private fun renderSuccessResponse(movies: List<Movie>?) {
        val adapter = HighestMovieAdapter(this, movies)
        activity_search_recycler_view.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchItem.expandActionView()
        searchView.requestFocus()

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                onBackPressed()
                return true
            }
        })

        searchViewTextSubscription = RxSearchView.queryTextChanges(searchView)
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribe { charSequence ->
                if (charSequence.length > 0) {
                    searchViewModel?.getSearchMovies(charSequence.toString())
                }
            }

        return true

    }


    override fun onDestroy() {
        RxUtils().unsubscribe(searchViewTextSubscription)
        super.onDestroy()
    }
}
