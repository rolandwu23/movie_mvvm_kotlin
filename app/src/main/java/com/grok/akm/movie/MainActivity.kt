package com.grok.akm.movie

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.grok.akm.movie.Utils.Constant
import com.grok.akm.movie.Utils.SortType
import com.grok.akm.movie.di.MyApplication
import com.grok.akm.movie.di.SortPreferences
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var preferences:SortPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).appComponent!!.doInjection(this)

        if(Constant().checkInternetConnection(this)){
           when (preferences.selectedOption){
               SortType.MOST_POPULAR.value -> showMostPopularMovies()
               SortType.HIGHEST_RATED.value -> showHighestMovies()
               SortType.NEWEST.value -> showNewestMovies()
               SortType.FAVORITES.value -> showFavourites()
           }
        }

    }

    fun showMostPopularMovies(){
        val intent = Intent(this,MostActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    fun showHighestMovies(){
        val intent = Intent(this,HighestActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    fun showNewestMovies(){
        val intent = Intent(this,NewestActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    fun showFavourites(){
        val intent = Intent(this,FavoritesActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}
