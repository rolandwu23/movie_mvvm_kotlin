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

class SearchViewModel(private val repository: Repository) : ViewModel() {

    private val searchMovies = MutableLiveData<ApiResponseMovie>()
    private var compositeDisposable = CompositeDisposable()


    fun getSearchMovies(searchQuery:String) {

        compositeDisposable.add(repository.searchMovies(searchQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { searchMovies.postValue(movieLoading()) }
            .subscribe(
                { result -> searchMovies.postValue(movieSuccess(result)) },
                { throwable -> searchMovies.postValue(movieError(throwable)) }
            ))
    }

    fun getSearchMoviesLiveData() = searchMovies

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}