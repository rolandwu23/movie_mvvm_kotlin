package com.grok.akm.movie.ViewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.grok.akm.movie.Model.pojo.Movie
import com.grok.akm.movie.Network.Status
import com.grok.akm.movie.Paging.HighestMovieDataSource
import com.grok.akm.movie.Paging.HighestMovieDataSourceFactory
import io.reactivex.disposables.CompositeDisposable

class HighestViewModel (private val repository: Repository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val highestDataSourceFactory = HighestMovieDataSourceFactory(repository,compositeDisposable)

    private val pagedListConfig = PagedList.Config.Builder().apply {
        setEnablePlaceholders(true)
        setInitialLoadSizeHint(10)
        setPageSize(10)
    }.build()

    private val highestMovies = LivePagedListBuilder(highestDataSourceFactory, pagedListConfig).build()

    private val highestLoadStatus = Transformations.switchMap(highestDataSourceFactory.getLiveData(),HighestMovieDataSource::getProgressLiveStatus)

    fun getHighestLoadStatus() : LiveData<Status> = highestLoadStatus

    fun getHighestMoviesLiveData() : LiveData<PagedList<Movie>> = highestMovies

    fun refresh() {
        highestDataSourceFactory.getLiveData().value?.invalidate()
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}