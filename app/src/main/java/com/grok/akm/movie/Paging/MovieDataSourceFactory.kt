package com.grok.akm.movie.Paging

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.grok.akm.movie.Model.pojo.Movie
import com.grok.akm.movie.ViewModel.Repository
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(private val repository: Repository,
                                    private val disposables: CompositeDisposable): DataSource.Factory<Int, Movie>() {


    private val mostpopularLiveData = MutableLiveData<MovieDataSource>()

    fun getLiveData() = mostpopularLiveData

    override fun create(): DataSource<Int, Movie> {
        val source = MovieDataSource(repository,disposables)
        mostpopularLiveData.postValue(source)
        return source
    }
}