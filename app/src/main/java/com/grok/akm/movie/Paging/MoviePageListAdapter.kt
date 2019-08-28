package com.grok.akm.movie.Paging

import android.app.Activity
import android.arch.paging.PagedListAdapter
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.graphics.Palette
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.grok.akm.movie.DetailsActivity
import com.grok.akm.movie.Model.pojo.Movie
import com.grok.akm.movie.R
import com.grok.akm.movie.Utils.Constant

open class MoviePageListAdapter(private val context: Context) :
    PagedListAdapter<Movie, MoviePageListAdapter.ViewHolder>(Movie.DIFF_CALLBACK) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val rootView = LayoutInflater.from(viewGroup.context).inflate(R.layout.movie, viewGroup, false)
        return ViewHolder(rootView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        viewHolder.movie = getItem(i)

        viewHolder.itemView.setOnClickListener(viewHolder)
        viewHolder.title.text = getItem(i)!!.title

        val options = RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .priority(Priority.HIGH)

        Glide.with(context)
            .asBitmap()
            .load(Constant().getPosterPath(getItem(i)!!.posterPath))
            .apply(options)
            .into(object : BitmapImageViewTarget(viewHolder.poster) {
                override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                    super.onResourceReady(bitmap, transition)
                    Palette.from(bitmap).generate { palette -> palette?.let { setBackgroundColor(it, viewHolder) } }
                }
            })
    }

    private fun setBackgroundColor(palette: Palette, holder: ViewHolder) {
        holder.titleBackground.setBackgroundColor(
            palette.getVibrantColor(
                context
                    .resources.getColor(R.color.black_translucent_60)
            )
        )
    }

    private fun detailsPath(movie: Movie?, view: View) {
        val intent = Intent(context, DetailsActivity::class.java)
        val extras = Bundle()
        extras.putParcelable(Constant.MOVIE, movie)
        intent.putExtras(extras)

        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
            context as Activity,

            // Now we provide a list of Pair items which contain the view we can transitioning
            // from, and the name of the view it is transitioning to, in the launched activity
            Pair<View, String>(
                view.findViewById(R.id.movie_poster),
                DetailsActivity.VIEW_NAME_HEADER_IMAGE
            ),
            Pair<View, String>(
                view.findViewById(R.id.movie_name),
                DetailsActivity.VIEW_NAME_HEADER_TITLE
            )
        )

        // Now we can start the Activity, providing the activity options as a bundle
        ActivityCompat.startActivity(context, intent, activityOptions.toBundle())

        //        context.startActivity(intent,
        //                ActivityOptions.makeSceneTransitionAnimation((Activity)context).toBundle());
    }

    open inner class ViewHolder(root:View) : RecyclerView.ViewHolder(root), View.OnClickListener {

        var poster: ImageView
        var titleBackground: View
        var title: TextView

        var movie: Movie? = null

        init {
            poster = root.findViewById<View>(R.id.movie_poster) as ImageView
            title = root.findViewById<View>(R.id.movie_name) as TextView
            titleBackground = root.findViewById(R.id.title_background) as View
        }

        override fun onClick(v: View) {
            detailsPath(movie, v)
        }


    }

}
