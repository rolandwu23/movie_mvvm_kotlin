package com.grok.akm.movie.ViewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.grok.akm.movie.Model.pojo.Movie
import com.grok.akm.movie.Room.FavoriteRepository

class FavoriteViewModel (application: Application) : AndroidViewModel(application) {

    private var repository:FavoriteRepository = FavoriteRepository(application)

    fun getFavorites() = repository.favorites

    fun setFavorite(movie: Movie) { repository.setFavorite(movie)}

    fun unFavorite(id:String) { repository.unFavorite(id)}

    fun isFavorite(id:String) = repository.isFavorite(id)
}