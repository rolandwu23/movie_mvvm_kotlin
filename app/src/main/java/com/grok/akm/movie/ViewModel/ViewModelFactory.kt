package com.grok.akm.movie.ViewModel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject

class ViewModelFactory
@Inject
constructor(private val repository: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return DetailsViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(NewestViewModel::class.java)) {
            return NewestViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(FragmentViewModel::class.java)) {
            return FragmentViewModel() as T
        }else if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(HighestViewModel::class.java)) {
            return HighestViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}