package com.grok.akm.movie.di

import com.grok.akm.movie.*
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, UtilModule::class, SortOptionsModule::class])
@Singleton
interface AppComponent {

    fun doInjection(mainActivity: MainActivity)

    fun doInjection(detailsActivity: DetailsActivity)

    fun doInjection(favoritesActivity: FavoritesActivity)

    fun doInjection(highestActivity: HighestActivity)

    fun doInjection(mostActivity: MostActivity)

    fun doInjection(newestActivity: NewestActivity)

    fun doInjection(searchActivity: SearchActivity)

    fun doInjection(sortingDialogFragment: SortingDialogFragment)

}