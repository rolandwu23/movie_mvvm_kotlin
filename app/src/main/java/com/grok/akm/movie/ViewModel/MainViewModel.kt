package com.grok.akm.movie.ViewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.grok.akm.movie.Model.pojo.Movie
import com.grok.akm.movie.Network.Status
import com.grok.akm.movie.Paging.MovieDataSource
import com.grok.akm.movie.Paging.MovieDataSourceFactory
import io.reactivex.disposables.CompositeDisposable

class MainViewModel (private val repository: Repository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val mostDataSourceFactory = MovieDataSourceFactory(repository,compositeDisposable)

    private val pagedListConfig = PagedList.Config.Builder().apply {
        setEnablePlaceholders(true)
        setInitialLoadSizeHint(10)
        setPageSize(10)
    }.build()

    private val mostMovies:LiveData<PagedList<Movie>> = LivePagedListBuilder(mostDataSourceFactory,pagedListConfig).build()

    private val mostLoadStatus:LiveData<Status> = Transformations.switchMap(mostDataSourceFactory.getLiveData(),MovieDataSource::getProgressLiveStatus)

    fun getMostLoadStatus() : LiveData<Status> = mostLoadStatus

    fun getMostMoviesLiveData() : LiveData<PagedList<Movie>> = mostMovies

    fun refresh() { mostDataSourceFactory.getLiveData().value?.invalidate()}


    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}