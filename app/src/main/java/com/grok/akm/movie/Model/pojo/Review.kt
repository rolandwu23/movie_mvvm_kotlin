package com.grok.akm.movie.Model.pojo

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("id") @Expose var id:String,
    @SerializedName("author") @Expose var author:String,
    @SerializedName("content") @Expose var content:String,
    @SerializedName("url") @Expose var url:String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readString(),
        author = parcel.readString(),
        content = parcel.readString(),
        url = parcel.readString()
    ) {
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(author)
        dest.writeString(content)
        dest.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Review> {
        override fun createFromParcel(parcel: Parcel): Review {
            return Review(parcel)
        }

        override fun newArray(size: Int): Array<Review?> {
            return arrayOfNulls(size)
        }
    }

}