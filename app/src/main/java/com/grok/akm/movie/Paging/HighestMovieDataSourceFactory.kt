package com.grok.akm.movie.Paging

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.grok.akm.movie.Model.pojo.Movie
import com.grok.akm.movie.ViewModel.Repository
import io.reactivex.disposables.CompositeDisposable

class HighestMovieDataSourceFactory(private val repository: Repository,
                                    private val disposables: CompositeDisposable): DataSource.Factory<Int, Movie>() {


    private val highestLiveData = MutableLiveData<HighestMovieDataSource>()

    fun getLiveData() = highestLiveData

    override fun create(): DataSource<Int, Movie> {
        val source = HighestMovieDataSource(repository,disposables)
        highestLiveData.postValue(source)
        return source
    }
}