package com.grok.akm.movie

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
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
import com.grok.akm.movie.Utils.SortType
import com.grok.akm.movie.ViewModel.FragmentViewModel
import com.grok.akm.movie.ViewModel.NewestViewModel
import com.grok.akm.movie.ViewModel.ViewModelFactory
import com.grok.akm.movie.di.MyApplication
import kotlinx.android.synthetic.main.activity_newest.*
import javax.inject.Inject

class NewestActivity : AppCompatActivity() , OnRefreshListener{

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var adapter:HighestMovieAdapter

    private var newestViewModel:NewestViewModel? = null

    private var fragmentViewModel:FragmentViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newest)

        setToolbar()

        (application as MyApplication).appComponent!!.doInjection(this)
        ButterKnife.bind(this)

        val layoutManager = GridLayoutManager(this, 2)
        activity_newest_recycler_view.layoutManager = layoutManager

        newestViewModel = ViewModelProviders.of(this, viewModelFactory).get(NewestViewModel::class.java)

        newestViewModel?.getNewesetMoviesLiveData()
            ?.observe(this, Observer<ApiResponseMovie> { this.consumeNewestMovieResponse(it) })

        newestViewModel?.getNewestMovies()

        fragmentViewModel = ViewModelProviders.of(this, viewModelFactory).get(FragmentViewModel::class.java)

        fragmentViewModel?.getStatusLiveData()?.observe(this, Observer<SortType> { this.showSortOptions(it) })

        swipe_refresh.setColorSchemeResources(R.color.colorAccent)

        swipe_refresh.setOnRefreshListener(this)
    }

    private fun setToolbar()
    {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        if (supportActionBar != null) {
            supportActionBar!!.setTitle(R.string.movie_guide)
            supportActionBar!!.setDisplayShowTitleEnabled(true)
        }
    }

    private fun renderSuccessResponse(movies: List<Movie>?) {
        swipe_refresh.isRefreshing = false
        shimmer_view_container.stopShimmer()
        shimmer_view_container.visibility = View.GONE
        adapter = HighestMovieAdapter(this, movies)
        activity_newest_recycler_view.adapter = adapter
    }

    private fun consumeNewestMovieResponse(apiResponse: ApiResponseMovie?) {

        when (apiResponse?.status) {

            Status.LOADING -> {
                swipe_refresh.isRefreshing = false
                shimmer_view_container.visibility = View.VISIBLE
                shimmer_view_container.startShimmer()
            }

            Status.SUCCESS -> renderSuccessResponse(apiResponse.data?.movies)

            Status.ERROR -> {
                swipe_refresh.isRefreshing = false
                shimmer_view_container.stopShimmer()
                shimmer_view_container.visibility = View.GONE
                Toast.makeText(this, resources.getString(R.string.errorString), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.getItemId()) {
            R.id.action_sort -> displaySortOptions()
            R.id.action_search -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displaySortOptions(){
        val sortingDialogFragment = SortingDialogFragment.newInstance()
        sortingDialogFragment.show(supportFragmentManager, "Select Quantity")
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun showSortOptions(sortType: SortType?) {
        when (sortType) {
            SortType.MOST_POPULAR -> showMostPopularMovies()
            SortType.HIGHEST_RATED -> showHighestMovies()
            SortType.FAVORITES -> showFavoriteMovies()
        }
    }

    private fun showMostPopularMovies() {
        val intent = Intent(this, MostActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun showHighestMovies() {
        val intent = Intent(this, HighestActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun showFavoriteMovies() {
        val intent = Intent(this, FavoritesActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    override fun onRefresh() { newestViewModel?.getNewestMovies()}


}
