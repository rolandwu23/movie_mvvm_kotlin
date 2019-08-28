package com.grok.akm.movie

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import butterknife.ButterKnife
import com.grok.akm.movie.Network.Status
import com.grok.akm.movie.Paging.MoviePageListAdapter
import com.grok.akm.movie.Utils.SortType
import com.grok.akm.movie.ViewModel.FragmentViewModel
import com.grok.akm.movie.ViewModel.HighestViewModel
import com.grok.akm.movie.ViewModel.ViewModelFactory
import com.grok.akm.movie.di.MyApplication
import kotlinx.android.synthetic.main.activity_highest.*
import javax.inject.Inject

class HighestActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var highestViewModel: HighestViewModel? = null

    private var fragmentViewModel: FragmentViewModel? = null

    lateinit var pageListAdapter: MoviePageListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_highest)

        setToolbar()

        (application as MyApplication).appComponent!!.doInjection(this)
        ButterKnife.bind(this)

        val layoutManager = GridLayoutManager(this, 2)
        activity_highest_recycler_view.layoutManager = layoutManager

        highestViewModel = ViewModelProviders.of(this, viewModelFactory).get(HighestViewModel::class.java)

        fragmentViewModel = ViewModelProviders.of(this, viewModelFactory).get(FragmentViewModel::class.java)

        fragmentViewModel?.getStatusLiveData()?.observe(this, Observer { this.showSortOptions(it) })

        swipe_refresh.setColorSchemeResources(R.color.colorAccent)

        swipe_refresh.setOnRefreshListener(this)

        init()
    }

    private fun setToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        if (supportActionBar != null) {
            supportActionBar!!.setTitle(R.string.movie_guide)
            supportActionBar!!.setDisplayShowTitleEnabled(true)
        }
    }

    private fun init() {

        pageListAdapter = MoviePageListAdapter(this)

        highestViewModel?.getHighestMoviesLiveData()?.observe(this, Observer { pageListAdapter.submitList(it) })

        pageListAdapter.notifyDataSetChanged()

        activity_highest_recycler_view.swapAdapter(pageListAdapter, true)

        highestViewModel?.getHighestLoadStatus()?.observe(this,Observer<Status> { this.showHighestMovies(it) })
    }

    private fun showHighestMovies(status: Status?){
        if (status == Status.LOADING) {
            swipe_refresh.isRefreshing = false
            shimmer_view_container.visibility = View.VISIBLE
            shimmer_view_container.startShimmer()
        } else if (status == Status.SUCCESS) {
            swipe_refresh.isRefreshing = false
            shimmer_view_container.stopShimmer()
            shimmer_view_container.visibility = View.GONE
        } else if (status == Status.ERROR) {
            swipe_refresh.isRefreshing = false
            shimmer_view_container.stopShimmer()
            shimmer_view_container.visibility = View.GONE
            Toast.makeText(this, resources.getString(R.string.errorString), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort -> displaySortOptions()
            R.id.action_search -> {
                val intent = Intent(this@HighestActivity, SearchActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun displaySortOptions() {
        val sortingDialogFragment = SortingDialogFragment.newInstance()
        sortingDialogFragment.show(supportFragmentManager, "Select Quantity")
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun showSortOptions(sortType: SortType?) {
        when (sortType) {
            SortType.MOST_POPULAR -> showMostPopularMovies()
            SortType.NEWEST -> showNewestMovies()
            SortType.FAVORITES -> showFavoriteMovies()
        }
    }

    private fun showMostPopularMovies() {
        val intent = Intent(this, MostActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun showNewestMovies() {
        val intent = Intent(this, NewestActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun showFavoriteMovies() {
        val intent = Intent(this, FavoritesActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    override fun onRefresh() {
        highestViewModel?.refresh()
    }
}
