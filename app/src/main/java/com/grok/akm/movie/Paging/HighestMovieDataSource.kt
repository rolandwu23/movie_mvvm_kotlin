package com.grok.akm.movie.Paging

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import com.grok.akm.movie.Model.pojo.Movie
import com.grok.akm.movie.Network.Status
import com.grok.akm.movie.ViewModel.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class HighestMovieDataSource(private val repository: Repository,
                             private val disposables: CompositeDisposable) : PageKeyedDataSource<Int, Movie>() {

    private val progressLiveStatus = MutableLiveData<Status>()


    fun getProgressLiveStatus() = progressLiveStatus


    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {

        disposables.add(
            repository.highestRatedMovies(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ d -> progressLiveStatus.postValue(Status.INITIAL) })
                .subscribe(
                    { result ->
                        progressLiveStatus.postValue(Status.SUCCESS)
                        callback.onResult(result.movies, null, 1)

                    },
                    { throwable -> progressLiveStatus.postValue(Status.ERROR) })
        )

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

        disposables.add(repository.highestRatedMovies(params.key + 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe({ d -> progressLiveStatus.postValue(Status.LOADING) })
            .subscribe(
                { result ->
                    val page = result.total_pages
                    progressLiveStatus.postValue(Status.SUCCESS)
                    callback.onResult(result.movies, if (params.key == page) null else params.key + 1)
                },
                { throwable -> progressLiveStatus.postValue(Status.ERROR) }
            ))
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

    }

}