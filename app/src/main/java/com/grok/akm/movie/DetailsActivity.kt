package com.grok.akm.movie

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.grok.akm.movie.Model.ApiResponseReview
import com.grok.akm.movie.Model.ApiResponseTrailer
import com.grok.akm.movie.Model.pojo.Movie
import com.grok.akm.movie.Model.pojo.Review
import com.grok.akm.movie.Model.pojo.Video
import com.grok.akm.movie.Network.Status
import com.grok.akm.movie.Utils.Constant
import com.grok.akm.movie.ViewModel.DetailsViewModel
import com.grok.akm.movie.ViewModel.FavoriteViewModel
import com.grok.akm.movie.ViewModel.ViewModelFactory
import com.grok.akm.movie.di.MyApplication
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.review.*
import kotlinx.android.synthetic.main.trailers_and_reviews.*
import kotlinx.android.synthetic.main.video.*
import javax.inject.Inject


class DetailsActivity : AppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var favoriteViewModel: FavoriteViewModel? = null

    private var movie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_details)

        movie = intent.getParcelableExtra(Constant.MOVIE)

        Log.e("Movie",movie.toString());

        (application as MyApplication).appComponent!!.doInjection(this)

        ViewCompat.setTransitionName(movie_poster, VIEW_NAME_HEADER_IMAGE)
        ViewCompat.setTransitionName(movie_name, VIEW_NAME_HEADER_TITLE)

        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)

        val detailsViewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel::class.java)

        detailsViewModel.getTrailers(movie!!.id)

        detailsViewModel.getTrailersLiveData().observe(this, Observer { this.consumeTrailerResponse(it) })

        detailsViewModel.getReviews(movie!!.id)

        detailsViewModel.getReviewsLiveData().observe(this, Observer { this.consumeReviewResponse(it) })

        setToolbar()

        showDetails(movie!!)

        showFavourite()

    }

    private fun setToolbar() {
        collapsing_toolbar.setContentScrimColor(ContextCompat.getColor(this, R.color.colorPrimary))
        collapsing_toolbar.title = getString(R.string.movie_details)
        collapsing_toolbar.setCollapsedTitleTextAppearance(R.style.CollapsedToolbar)
        collapsing_toolbar.setExpandedTitleTextAppearance(R.style.ExpandedToolbar)
        collapsing_toolbar.isTitleEnabled = true

        if (toolbar != null) {
            (this as AppCompatActivity).setSupportActionBar(toolbar)

            val actionBar = (this as AppCompatActivity).supportActionBar
            actionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            // Don't inflate. Tablet is in landscape mode.
        }
    }

    private fun showDetails(movie: Movie) {
        Glide.with(this)
            .load(Constant().getBackdropPath(movie.backdropPath))
            .into(movie_poster)
        movie_name.text = movie.title
        movie_year.text = String.format(getString(R.string.release_date), movie.releaseDate)
        movie_rating.text = String.format(getString(R.string.rating), movie.voteAverage.toString())
        movie_description.text = movie.overview
    }

    private fun consumeTrailerResponse(apiResponseTrailer: ApiResponseTrailer?) {
        when (apiResponseTrailer?.status) {

            Status.LOADING -> Snackbar.make(findViewById(android.R.id.content), "Loading Trailers...", Snackbar.LENGTH_SHORT)
                .show()

            Status.SUCCESS -> showTrailers(apiResponseTrailer.data?.videos)

            Status.ERROR -> Toast.makeText(
                this@DetailsActivity,
                resources.getString(R.string.errorString),
                Toast.LENGTH_SHORT
            ).show()

            else -> {
            }
        }
    }

    private fun consumeReviewResponse(apiResponseReview: ApiResponseReview?) {
        when (apiResponseReview?.status) {

            Status.LOADING -> Snackbar.make(findViewById(android.R.id.content), "Loading Reviews...", Snackbar.LENGTH_SHORT)
                .show()

            Status.SUCCESS -> showReviews(apiResponseReview.data?.reviews)

            Status.ERROR -> Toast.makeText(
                this@DetailsActivity,
                resources.getString(R.string.errorString),
                Toast.LENGTH_SHORT
            ).show()

            else -> {
            }
        }
    }

    private fun showTrailers(videos: List<Video>?) {

        if (videos != null) {
            if (videos.isEmpty()) {
                trailers_label.visibility = View.GONE
                this.trailers.visibility = View.GONE
                trailers_container.visibility = View.GONE

            } else {
                trailers_label.visibility = View.VISIBLE
                this.trailers.visibility = View.VISIBLE
                trailers_container.visibility = View.VISIBLE

                this.trailers.removeAllViews()
                val inflater = this.layoutInflater
                val options = RequestOptions()
                    .placeholder(R.color.colorPrimary)
                    .centerCrop()
                    .override(150, 150)

                for (trailer in videos) {
                    val thumbContainer = inflater.inflate(R.layout.video, this.trailers, false)
                    val thumbView = thumbContainer.findViewById<ImageView>(R.id.video_thumb)
                    thumbView.setTag(R.id.glide_tag, Constant().getUrl(trailer))
                    thumbView.requestLayout()
                    thumbView.setOnClickListener(this)
                    Glide.with(this)
                        .load(Constant().getThumbnailUrl(trailer))
                        .apply(options)
                        .into(thumbView)
                    this.trailers.addView(thumbContainer)
                }
            }
        }
    }

    private fun showReviews(reviews: List<Review>?) {

        if (reviews != null) {
            if (reviews.isEmpty()) {
                reviews_label.visibility = View.GONE
                review_description.visibility = View.GONE
            } else {
                reviews_label.visibility = View.VISIBLE
                review_description.visibility = View.VISIBLE

                review_description.removeAllViews()
                val inflater = this.layoutInflater
                for (review in reviews) {
                    val reviewContainer = inflater.inflate(R.layout.review, review_description, false) as ViewGroup
                    val reviewAuthor = reviewContainer.findViewById<TextView>(R.id.review_author)
                    val reviewContent = reviewContainer.findViewById<TextView>(R.id.review_content)
                    reviewAuthor.text = review.author
                    reviewContent.text = review.content
                    reviewContent.setOnClickListener(this)
                    review_description.addView(reviewContainer)
                }
            }
        }
    }

    private fun showFavourite() {
        if (favoriteViewModel?.isFavorite(movie!!.id)!!) {
            favorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp))
        } else {
            favorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp))
        }
    }

    private fun onReviewClick(view: TextView) {
        if (view.maxLines == 5) {
            view.maxLines = 500
        } else {
            view.maxLines = 5
        }
    }

    private fun onThumbnailClick(view: View) {
        val videoUrl = view.getTag(R.id.glide_tag) as String
        val playVideoIntent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
        startActivity(playVideoIntent)
    }

    private fun onFavouriteClick() {

        val favourite = favoriteViewModel?.isFavorite(movie!!.id)!!
        if (favourite) {
            favoriteViewModel?.unFavorite(movie!!.id)
            favorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp))
        } else {
            favoriteViewModel?.setFavorite(movie!!)
            favorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    override fun onClick(view: View) {
        when(view){
            video_thumb -> onThumbnailClick(view)
            review_content -> onReviewClick(view as TextView)
            favorite -> onFavouriteClick()
        }
    }

    companion object {

        // View name of the header image. Used for activity scene transitions
        val VIEW_NAME_HEADER_IMAGE = "detail:header:image"

        // View name of the header title. Used for activity scene transitions
        val VIEW_NAME_HEADER_TITLE = "detail:header:title"
    }


}
