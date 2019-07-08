package com.grok.akm.movie.Room

import android.app.Application
import android.arch.lifecycle.LiveData
import android.util.Log
import com.grok.akm.movie.Model.pojo.Movie
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class FavoriteRepository(application: Application) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val favoriteDao: FavoriteDao?

    val favorites: LiveData<List<Movie>>?
        get() = favoriteDao?.getFavorites()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        favoriteDao = db?.favoriteDao()
    }

    fun setFavorite(movie: Movie) {
        launch { setFavoriteBG(movie) }
    }

    private suspend fun setFavoriteBG(movie: Movie){
        withContext(Dispatchers.IO){
            favoriteDao?.setFavorite(movie)
        }
    }

    fun unFavorite(id: String) {
        launch { unFavoriteBG(id) }
    }

    private suspend fun unFavoriteBG(id:String) {
         withContext(Dispatchers.IO){
             favoriteDao?.unFavorite(id)
        }
    }

    fun isFavorite(id: String): Boolean? {
        var b: Boolean? = null
        runBlocking { b = isFavoriteBG(id) }
        return b
    }

    private suspend fun isFavoriteBG(id:String) : Boolean {
        return withContext(Dispatchers.IO)  {
            val boolean = favoriteDao?.isFavorite(id)!! > 0
            Log.e("Coroutine Boolean",""+boolean)
             boolean
        }
    }

}
