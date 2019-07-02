package com.grok.akm.movie.Model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieWrapper(
    @SerializedName("results") @Expose var movies:List<Movie>,
    @SerializedName("total_pages") @Expose var total_pages:Int
)