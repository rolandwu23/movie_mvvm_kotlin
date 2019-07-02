package com.grok.akm.movie.ViewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.grok.akm.movie.Model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailsViewModel(private val repository: Repository) : ViewModel() {

    private var trailers = MutableLiveData<ApiResponseTrailer>()
    private val reviews = MutableLiveData<ApiResponseReview>()
    private var compositeDisposable = CompositeDisposable()


    fun getTrailers(movieId:String) {

        compositeDisposable.add(repository.getTrailer(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { trailers.postValue(trailerLoading()) }
            .subscribe(
                { result -> trailers.postValue(trailerSuccess(result)) },
                { throwable -> trailers.postValue(trailerError(throwable)) }
            ))
    }


    fun getTrailersLiveData() = trailers

    fun getReviews(movieId:String) {

        compositeDisposable.add(repository.getReviews(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { reviews.postValue(reviewLoading()) }
            .subscribe(
                { result -> reviews.postValue(reviewSuccess(result)) },
                { throwable -> reviews.postValue(reviewError(throwable)) }
            ))
    }


    fun getReviewsLiveData() = reviews

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}