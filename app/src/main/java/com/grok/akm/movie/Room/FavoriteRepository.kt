package com.grok.akm.movie.Room

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.grok.akm.movie.Model.pojo.Movie
import java.util.concurrent.ExecutionException

class FavoriteRepository(application: Application) {

    private val favoriteDao: FavoriteDao?

    val favorites: LiveData<List<Movie>>?
        get() = favoriteDao?.getFavorites()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        favoriteDao = db?.favoriteDao()
    }

    fun setFavorite(movie: Movie) {
        insertAsyncTask(favoriteDao).execute(movie)
    }

    private class insertAsyncTask internal constructor(private val mAsyncTaskDao: FavoriteDao?) :
        AsyncTask<Movie, Void, Void>() {

        override fun doInBackground(vararg params: Movie): Void? {
            mAsyncTaskDao?.setFavorite(params[0])
            return null
        }
    }

    fun unFavorite(id: String) {
        unAsyncTask(favoriteDao).execute(id)
    }

    fun isFavorite(id: String): Boolean? {
        var b: Boolean? = null
        try {
            b = checkAsyncTask(favoriteDao).execute(id).get()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return b
    }

    private class checkAsyncTask internal constructor(private val mAsyncTaskDao: FavoriteDao?) :
        AsyncTask<String, Void, Boolean>() {

        override fun doInBackground(vararg params: String): Boolean? {
            return mAsyncTaskDao?.isFavorite(params[0])!! > 0
        }
    }

    private class unAsyncTask internal constructor(private val mAsyncTaskDao: FavoriteDao?) :
        AsyncTask<String, Void, Void>() {

        override fun doInBackground(vararg params: String): Void? {
            mAsyncTaskDao?.unFavorite(params[0])
            return null
        }
    }


}
