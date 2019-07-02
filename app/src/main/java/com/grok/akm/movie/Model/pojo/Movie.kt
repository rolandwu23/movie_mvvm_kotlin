package com.grok.akm.movie.Model.pojo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.NonNull
import android.support.v7.util.DiffUtil
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favorite_table")
data class Movie (
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id") var id:String,

    @SerializedName("overview")
    @Expose
    @ColumnInfo(name = "overview")
    var overview:String,

    @SerializedName("release_date")
    @Expose
    @ColumnInfo(name = "releaseDate")
    var releaseDate:String,

    @SerializedName("poster_path")
    @Expose
    @ColumnInfo(name = "posterPath")
    var posterPath:String,

    @SerializedName("backdrop_path")
    @Expose
    @ColumnInfo(name = "backdropPath")
    var backdropPath:String,

    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title")
    var title:String,

    @SerializedName("vote_average")
    @Expose
    @ColumnInfo(name = "voteAverage")
    var voteAverage: Double
) : Parcelable
{
    constructor(parcel: Parcel) : this(
        id = parcel.readString(),
        overview = parcel.readString(),
        releaseDate = parcel.readString(),
        posterPath = parcel.readString(),
        backdropPath = parcel.readString(),
        title = parcel.readString(),
        voteAverage = parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(overview)
        parcel.writeString(releaseDate)
        parcel.writeString(posterPath)
        parcel.writeString(backdropPath)
        parcel.writeString(title)
        parcel.writeDouble(voteAverage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }

        var DIFF_CALLBACK: DiffUtil.ItemCallback<Movie> = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun equals(obj: Any?): Boolean {
        if (obj === this)
            return true

        val article = obj as Movie?
        return article!!.id == this.id
    }

}

