package com.grok.akm.movie.Model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReviewWrapper(
    @SerializedName("results") @Expose var reviews:List<Review>
)