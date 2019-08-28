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
import com.grok.akm.movie.ViewModel.MainViewModel
import com.grok.akm.movie.ViewModel.ViewModelFactory
import com.grok.akm.movie.di.MyApplication
import com.grok.akm.movie.di.SortPreferences
import kotlinx.android.synthetic.main.activity_most.*
import javax.inject.Inject

class MostActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var pref: SortPreferences

    private var mainViewModel: MainViewModel? = null

    private var fragmentViewModel: FragmentViewModel? = null

    //    SearchViewModel searchViewModel;

    private lateinit var pageListAdapter: MoviePageListAdapter

    //    private Disposable searchViewTextSubscription;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_most)
        setToolbar()

        (application as MyApplication).appComponent!!.doInjection(this)
        ButterKnife.bind(this)

        val layoutManager = GridLayoutManager(this, 2)
        activity_most_recycler_view.layoutManager = layoutManager

        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        //        searchViewModel = ViewModelProviders.of(this,viewModelFactory).get(SearchViewModel.class);

        //        searchViewModel.getSearchMoviesLiveData().observe(this, this::consumeSearchMovieResponse);

        fragmentViewModel = ViewModelProviders.of(this, viewModelFactory).get(FragmentViewModel::class.java)

        fragmentViewModel?.getStatusLiveData()?.observe(this, Observer<SortType> { this.showSortOptions(it) })

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

        mainViewModel?.getMostMoviesLiveData()?.observe(this, Observer { pageListAdapter.submitList(it) })

        pageListAdapter.notifyDataSetChanged()

        activity_most_recycler_view.swapAdapter(pageListAdapter, true)

        mainViewModel?.getMostLoadStatus()?.observe(this,Observer<Status> { this.showMostMovies(it) })
    }

    private fun showMostMovies(status: Status?){
        if (status  == Status.LOADING) {
            swipe_refresh.isRefreshing = false
            swipe_refresh.visibility = View.VISIBLE
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

    private fun showFavourites() {
        val intent = Intent(this, FavoritesActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun showSortOptions(sortType: SortType?) {
        when (sortType) {
            SortType.HIGHEST_RATED -> showHighestMovies()
            SortType.NEWEST -> showNewestMovies()
            SortType.FAVORITES -> showFavourites()
        }
    }

    //    private void consumeSearchMovieResponse(ApiResponseMovie apiResponse) {
    //
    //        switch (apiResponse.status) {
    //
    //            case LOADING:
    //                Snackbar.make(findViewById(android.R.id.content), "Loading Movies...", Snackbar.LENGTH_SHORT)
    //                        .show();
    //                break;
    //
    //            case SUCCESS:
    //                renderSuccessResponse(apiResponse.data.getMovieList());
    //                break;
    //
    //            case ERROR:
    //                shimmerFrameLayout.stopShimmer();
    //                shimmerFrameLayout.setVisibility(View.GONE);
    //                Toast.makeText(MostActivity.this,getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
    //                break;
    //
    //            default:
    //                break;
    //        }
    //    }
    //
    //    private void renderSuccessResponse(List<Movie> movies){
    //        shimmerFrameLayout.stopShimmer();
    //        shimmerFrameLayout.setVisibility(View.GONE);
    //        HighestMovieAdapter adapter = new HighestMovieAdapter(this,movies);
    //        recyclerView.swapAdapter(adapter,true);
    //    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        //        final MenuItem searchItem = menu.findItem(R.id.action_search);
        //        final SearchView searchView = (SearchView) searchItem.getActionView();
        //
        //
        //        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
        //            @Override
        //            public boolean onMenuItemActionExpand(MenuItem item) {
        //                return true;
        //            }
        //
        //            @Override
        //            public boolean onMenuItemActionCollapse(MenuItem item) {
        //                recyclerView.swapAdapter(pageListAdapter,false);
        //                return true;
        //            }
        //        });
        //
        //        searchViewTextSubscription = RxSearchView.queryTextChanges(searchView)
        //                .debounce(500, TimeUnit.MILLISECONDS)
        //                .subscribe(charSequence -> {
        //                    if (charSequence.length() > 0) {
        //                        searchViewModel.getSearchMovies(charSequence.toString());
        //                    }
        //                });

        //        return true;
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort -> displaySortOptions()
            R.id.action_search -> {
                val intent = Intent(this@MostActivity, SearchActivity::class.java)
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
        //        RxUtils.unsubscribe(searchViewTextSubscription);
        super.onDestroy()
    }

    override fun onRefresh() {
        mainViewModel?.refresh()
    }
}
