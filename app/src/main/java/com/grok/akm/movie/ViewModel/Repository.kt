package com.grok.akm.movie.ViewModel

import android.annotation.SuppressLint
import com.grok.akm.movie.Model.pojo.MovieWrapper
import com.grok.akm.movie.Network.ApiCallInterface
import io.reactivex.Observable
import java.text.SimpleDateFormat
import java.util.*

class Repository(private val apiCallInterface: ApiCallInterface){

    @SuppressLint("SimpleDateFormat")
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    private val NEWEST_MIN_VOTE_COUNT = 50

    fun executeMovies(page:Int) = apiCallInterface.popularMovies(page)

    fun searchMovies(searchQuery:String) = apiCallInterface.searchMovies(searchQuery)

    fun highestRatedMovies(page: Int) = apiCallInterface.highestRatingMovies(page)

    fun newestMovies() : Observable<MovieWrapper> {
        val cal = Calendar.getInstance()
        val maxReleaseDate = dateFormat.format(cal.time)
        return apiCallInterface.newestMovies(maxReleaseDate,NEWEST_MIN_VOTE_COUNT)

    }

    fun getTrailer(movieId:String) = apiCallInterface.trailers(movieId)

    fun getReviews(movieId: String) = apiCallInterface.reviews(movieId)


}