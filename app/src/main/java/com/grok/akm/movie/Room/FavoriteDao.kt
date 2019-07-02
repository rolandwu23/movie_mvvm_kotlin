package com.grok.akm.movie.Room

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.grok.akm.movie.Model.pojo.Movie


@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun setFavorite(movie: Movie)

    @Query("SELECT * from favorite_table ORDER BY id ASC")
    fun getFavorites() : LiveData<List<Movie>>

    @Query("DELETE FROM favorite_table where id == :Id")
    fun unFavorite(Id:String)

    @Query("SELECT count(*) from favorite_table where id == :Id")
    fun isFavorite(Id:String) : Int

    @Query("DELETE FROM favorite_table")
    fun deleteAll()
}