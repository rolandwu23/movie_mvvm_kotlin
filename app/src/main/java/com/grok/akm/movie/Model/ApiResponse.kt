package com.grok.akm.movie.Model

import com.grok.akm.movie.Model.pojo.MovieWrapper
import com.grok.akm.movie.Model.pojo.ReviewWrapper
import com.grok.akm.movie.Model.pojo.VideoWrapper
import com.grok.akm.movie.Network.Status


// Movie
data class ApiResponseMovie (var status: Status?, var data: MovieWrapper?, var error:Throwable? )

fun movieLoading() = ApiResponseMovie(Status.LOADING, null, null)

fun movieSuccess(body: MovieWrapper) = ApiResponseMovie(Status.SUCCESS, body, null)

fun movieError(error: Throwable) = ApiResponseMovie(Status.ERROR, null, error)


// Review
data class ApiResponseReview (var status: Status?, var data: ReviewWrapper?, var error:Throwable? )

fun reviewLoading() = ApiResponseReview(Status.LOADING, null, null)

fun reviewSuccess(body: ReviewWrapper) = ApiResponseReview(Status.SUCCESS, body, null)

fun reviewError(error: Throwable) = ApiResponseReview(Status.ERROR, null, error)


// Trailer
data class ApiResponseTrailer (var status: Status?, var data: VideoWrapper?, var error:Throwable? )

fun trailerLoading() = ApiResponseTrailer(Status.LOADING, null, null)

fun trailerSuccess(body: VideoWrapper) = ApiResponseTrailer(Status.SUCCESS, body, null)

fun trailerError(error: Throwable) = ApiResponseTrailer(Status.ERROR, null, error)

