package com.grok.akm.movie

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import butterknife.ButterKnife
import com.grok.akm.movie.Model.pojo.Movie
import com.grok.akm.movie.Paging.HighestMovieAdapter
import com.grok.akm.movie.Utils.SortType
import com.grok.akm.movie.ViewModel.FavoriteViewModel
import com.grok.akm.movie.ViewModel.FragmentViewModel
import com.grok.akm.movie.ViewModel.ViewModelFactory
import com.grok.akm.movie.di.MyApplication
import kotlinx.android.synthetic.main.activity_favorites.*
import javax.inject.Inject

class FavoritesActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var favoriteViewModel: FavoriteViewModel? = null

    private var fragmentViewModel: FragmentViewModel? = null

    lateinit var adapter: HighestMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        setToolbar()

        (application as MyApplication).appComponent!!.doInjection(this)
        ButterKnife.bind(this)

        val layoutManager = GridLayoutManager(this, 2)
        activity_favorites_recycler_view.layoutManager = layoutManager

        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)

        favoriteViewModel?.getFavorites()?.observe(this, Observer<List<Movie>> { this.renderSuccessResponse(it) })

        fragmentViewModel = ViewModelProviders.of(this, viewModelFactory).get(FragmentViewModel::class.java)

        fragmentViewModel?.getStatusLiveData()?.observe(this, Observer<SortType> { this.showSortOptions(it) })
    }

    private fun setToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        if (supportActionBar != null) {
            supportActionBar!!.setTitle(R.string.movie_guide)
            supportActionBar!!.setDisplayShowTitleEnabled(true)
        }
    }

    private fun renderSuccessResponse(movies: List<Movie>?) {
        shimmer_view_container.stopShimmer()
        shimmer_view_container.visibility = View.GONE
        adapter = HighestMovieAdapter(this, movies)
        activity_favorites_recycler_view.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort -> displaySortOptions()
            R.id.action_search -> {
                val intent = Intent(this@FavoritesActivity, SearchActivity::class.java)
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
            SortType.HIGHEST_RATED -> showHighestMovies()
            SortType.NEWEST -> showNewestMovies()
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

    private fun showNewestMovies() {
        val intent = Intent(this, NewestActivity::class.java)
        startActivity(intent)
        this.finish()
    }

}
