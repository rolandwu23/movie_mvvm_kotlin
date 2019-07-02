package com.grok.akm.movie.ViewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.grok.akm.movie.Utils.SortType

class FragmentViewModel : ViewModel() {

    private val statusLiveData = MutableLiveData<SortType>()

    fun setStatusLiveData(status:SortType) { statusLiveData.value = status }

    fun getStatusLiveData() = statusLiveData

}