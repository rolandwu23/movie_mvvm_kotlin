package com.grok.akm.movie.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SortOptionsModule(private val context: Context) {

    internal val sortPref: SortPreferences
        @Provides
        @Singleton
        get() = SortPreferences(context)


//    @Provides
//    @Singleton
//    fun getSortPref() = SortPreferences(context)

}
