package com.grok.akm.movie.Utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.grok.akm.movie.Model.pojo.Video

class Constant {

    companion object {
        const val BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w342"
        const val BASE_BACKDROP_PATH = "http://image.tmdb.org/t/p/w780"
        const val YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%1\$s"
        const val YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%1\$s/0.jpg"

        const val MOVIE = "movie"
        const val EMPTY = ""

        const val SITE_YOUTUBE:String = "YouTube"
    }

    fun getPosterPath(posterPath:String?) = BASE_POSTER_PATH + posterPath

    fun getBackdropPath(posterPath: String) = BASE_BACKDROP_PATH + posterPath

    fun checkInternetConnection(context: Context): Boolean {
        val connectivity = context
            .getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?

        if(connectivity == null){
            return false
        } else{
            val info = connectivity.allNetworkInfo
            if (info != null) {
                for (i in info.indices) {
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
        }
        return false
    }

    fun getUrl(video: Video) = if(SITE_YOUTUBE.contentEquals(video.site)){
        String.format(YOUTUBE_VIDEO_URL,video.videoId)
    }else{
        EMPTY
    }

    fun getThumbnailUrl(video: Video) = if(SITE_YOUTUBE.contentEquals(video.site)){
        String.format(YOUTUBE_THUMBNAIL_URL,video.videoId)
    }else{
        EMPTY
    }
}