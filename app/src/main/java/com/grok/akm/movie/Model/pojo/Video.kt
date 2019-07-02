package com.grok.akm.movie.Model.pojo

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.grok.akm.movie.Utils.Constant


data class Video(
    @SerializedName("id") @Expose var id:String,
    @SerializedName("name") @Expose var name:String,
    @SerializedName("site") @Expose var site:String,
    @SerializedName("key") @Expose var videoId:String,
    @SerializedName("size") @Expose var size:Int,
    @SerializedName("type") @Expose var type:String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readString(),
        name = parcel.readString(),
        site = parcel.readString(),
        videoId = parcel.readString(),
        size = parcel.readInt(),
        type = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(site)
        parcel.writeString(videoId)
        parcel.writeInt(size)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Video> {
        override fun createFromParcel(parcel: Parcel): Video {
            return Video(parcel)
        }

        override fun newArray(size: Int): Array<Video?> {
            return arrayOfNulls(size)
        }
    }


}


