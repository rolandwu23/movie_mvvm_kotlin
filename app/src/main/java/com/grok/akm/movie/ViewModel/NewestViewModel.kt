package com.grok.akm.movie.ViewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.grok.akm.movie.Model.ApiResponseMovie
import com.grok.akm.movie.Model.movieError
import com.grok.akm.movie.Model.movieLoading
import com.grok.akm.movie.Model.movieSuccess
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NewestViewModel(private val repository: Repository) : ViewModel() {

    private val newestMovies = MutableLiveData<ApiResponseMovie>()
    private var compositeDisposable = CompositeDisposable()


    fun getNewestMovies() {

        compositeDisposable.add(repository.newestMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { newestMovies.postValue(movieLoading()) }
            .subscribe(
                { result -> newestMovies.postValue(movieSuccess(result)) },
                { throwable -> newestMovies.postValue(movieError(throwable)) }
            ))
    }

    fun getNewesetMoviesLiveData() = newestMovies

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}