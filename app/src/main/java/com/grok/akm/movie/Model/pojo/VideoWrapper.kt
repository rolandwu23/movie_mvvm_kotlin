package com.grok.akm.movie.Model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VideoWrapper(
    @SerializedName("results") @Expose var videos:List<Video>
)